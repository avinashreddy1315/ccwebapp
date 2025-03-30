package com.csye6225.controller;

import com.csye6225.models.Book;
import com.csye6225.models.BookImage;
import com.csye6225.repository.BookRepository;
import com.csye6225.repository.ImageRepository;
import com.csye6225.services.FileHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController("BookController")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;


    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private StatsDClient statsDClient;

    private static final String PNG = "image/png";
    private static final String JPG = "image/jpg";
    private static final String JPEG = "image/jpeg";

   // LoggerUtility logger=new LoggerUtility();
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * Get method to get all the books from the database
     * @param request
     * @param response
     * @return Json - All Books
     *
     */

    @RequestMapping(value = "/book", method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String GetBooks(HttpServletRequest request, HttpServletResponse response){
        statsDClient.incrementCounter("endpoint.book.api.get");
        try {
            List<Book> books = (List) bookRepository.findAll();
            for (Book book : books){
                    if(book.getBookImage()!=null){

               book.getBookImage().setUrl(fileHandler.getFile(book.getBookImage()));

                    }
         }  String json = new Gson().toJson(books);
            return json;

        }
        catch (Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "Exception occured! Check log");
            return jsonObject.toString();
        }

    }

    /**
     * Post method  to save a book in the database
     * @param request
     * @param response
     * @param book
     * @return Json of the created book
     */

    @RequestMapping(value = "/book", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String SaveBook(HttpServletRequest request, HttpServletResponse response, @RequestBody Book book){
        statsDClient.incrementCounter("endpoint.book.api.post");
        JsonObject jsonObject = new JsonObject();
        try {
            if(book.getTitle()!=null && book.getAuthor()!=null && book.getIsbn()!=null && book.getQuantity()!=0) {
                UUID id = UUID.randomUUID(); // Generating UUID for Book Id
                book.setId(id.toString());
                bookRepository.save(book);
                String json = new Gson().toJson(book);
                response.setStatus(HttpServletResponse.SC_CREATED);
                logger.info("created books");
                return json;

            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return jsonObject.toString();
            }
        }
        catch (Exception ex){
            logger.info("created books");
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
            return jsonObject.toString();
        }

    }

    /**
     * Put method to update a book
     * @param request
     * @param response
     * @param bookReq
     * @return response status only
     */

    @RequestMapping(value = "/book", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public String UpdateBook(HttpServletRequest request, HttpServletResponse response, @RequestBody Book bookReq){
        statsDClient.incrementCounter("endpoint.book.api.put");
        JsonObject jsonObject = new JsonObject();
        try {
            Book book = bookRepository.findById(bookReq.getId());
            if (book != null) {
                if(book.getTitle()!=null && book.getAuthor()!=null && book.getIsbn()!=null && book.getQuantity()!=0) {
                    book.setTitle(bookReq.getTitle());
                    book.setAuthor(bookReq.getAuthor());
                    book.setIsbn(bookReq.getIsbn());
                    book.setQuantity(bookReq.getQuantity());
                    bookRepository.save(book);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        catch (Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
        }
        return jsonObject.toString();
    }

    /**
     * Get method to get book by Id
     * @param id
     * @param response
     * @return Json
     * @throws IOException
     */

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String GetBookById(@PathVariable("id") String id, HttpServletResponse response) throws IOException{
        statsDClient.incrementCounter("endpoint.book.id.api.get");
        JsonObject jsonObject = new JsonObject();
        try {
            Book book = bookRepository.findById(id);
            if(book.getBookImage()!=null){
                book.getBookImage().setUrl(fileHandler.getFile(book.getBookImage()));

            }
//
            String json = new Gson().toJson(book);
            if (book != null) {

                response.setStatus(HttpServletResponse.SC_OK);
                return json;
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return jsonObject.toString();
            }
        }
        catch (Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
            return jsonObject.toString();
        }

    }

    /**
     * Delete method to delete a book by Id
     * @param id
     * @param response
     * @return response status only
     * @throws IOException
     */

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE, produces="application/json")
    @ResponseBody
    public String DeleteBookById(@PathVariable("id") String id, HttpServletResponse response) throws IOException{
        statsDClient.incrementCounter("endpoint.book.id.api.delete");
        JsonObject jsonObject = new JsonObject();
        try {
            Book book = bookRepository.findById(id);
            if (book != null) {
                BookImage bookImage = imageRepository.findById(book.getBookImage().getId());
                logger.info("before");
                if (bookImage != null) {
                    logger.info("inside");

                    fileHandler.deleteFile(bookImage);
                    logger.info("After");
                    book.setBookImage(null);
                }


                bookRepository.delete(book);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            } else {

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        catch (Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
        }

        return jsonObject.toString();

    }


    /**
     * Post method to post a image of a book by id
     * @param "idBook"
     * @param file
     * @param response
     * @return response status only
     * @throws Exception
     */

    @RequestMapping(value = "/book/{idBook}/image", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String ImageUpload(@PathVariable("idBook")String id, @RequestParam MultipartFile file, HttpServletResponse response) throws Exception {
        statsDClient.incrementCounter("endpoint.book.idBook.image.api.post");
        JsonObject jsonObject = new JsonObject();

        Book book = bookRepository.findById(id);
        if(book!=null)
        {
                   if (fileCheck(file.getContentType()))
                    {
                            BookImage bookImage = new BookImage();
                         //   String photoNewName =  generateFileName(file);

                          String photoNewName =  file.getOriginalFilename();

                            UUID Id = UUID.randomUUID(); // Generating UUID for Bookimage Id
                            bookImage.setId(Id.toString());

                            book.setBookImage(bookImage);

                        String filePath = fileHandler.uploadFile(file,photoNewName);

                        bookImage.setUrl(filePath);
                        bookRepository.save(book);
                        String json = new Gson().toJson(bookImage);

                        response.setStatus(HttpServletResponse.SC_OK);
                        return json;
                    }else
                        {

                        jsonObject.addProperty("message", "Select Correct format");

                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                        }


        }else {

                    jsonObject.addProperty("message", "Book not found!");
                     response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

               }

        return jsonObject.toString();
    }


    @RequestMapping(value = "/book/{idBook}/image/{idImage}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String GetImage(@PathVariable("idBook")String id,@PathVariable("idImage")String idImage,HttpServletResponse response) throws Exception {
        statsDClient.incrementCounter("endpoint.book.idBook.image.idImage.api.get");
        JsonObject jsonObject = new JsonObject();
        try {
            Book book =bookRepository.findById(id);
            BookImage bookImage = imageRepository.findById(idImage);

            bookImage.setUrl(fileHandler.getFile(bookImage));

            String json = new Gson().toJson(bookImage);

            if (book!=null && bookImage != null) {

                response.setStatus(HttpServletResponse.SC_OK);
                return json;
            }else{

                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return jsonObject.toString();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
            return jsonObject.toString();
        }


    }

    @RequestMapping(value = "/book/{idBook}/image/{idImage}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public String UpdateImage(@PathVariable("idBook")String id,@PathVariable("idImage")String idImage,@RequestParam MultipartFile file,HttpServletResponse response) throws Exception {
        statsDClient.incrementCounter("endpoint.book.idBook.image.idImage.api.put");
        JsonObject jsonObject = new JsonObject();
        try {
            Book book = bookRepository.findById(id);
            if (book != null) {

                BookImage bookImage = imageRepository.findById(idImage);
                if (bookImage != null) {

                    String photoNewName = generateFileName(file);

                    bookImage.setUrl(photoNewName);

                    book.setBookImage(bookImage);
                    fileHandler.uploadFile(file, photoNewName);

                    bookRepository.save(book);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);

                } else {

                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                       }
            } else {


                jsonObject.addProperty("message", "Book not found!");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }

        }catch (Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
        }
        return jsonObject.toString();
}



    @RequestMapping(value = "/book/{idBook}/image/{idImage}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String DeleteImageById(@PathVariable("idBook")String id,@PathVariable("idImage")String idImage,HttpServletResponse response) throws Exception{
        statsDClient.incrementCounter("endpoint.book.idBook.image.idImage.api.delete");
        JsonObject jsonObject = new JsonObject();
        try {
            Book book = bookRepository.findById(id);
            if (book != null) {
                BookImage bookImage = imageRepository.findById(idImage);
                logger.info("before");
                if (bookImage != null) {
                    logger.info("inside");

                    fileHandler.deleteFile(bookImage);
                    logger.info("After");

//                    File f = new File(bookImage.getUrl());
//                    if (f.exists()){
//                        f.delete();
//                    }
                    book.setBookImage(null);



                    imageRepository.delete(bookImage);

                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);

                } else {

                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }

        }catch (Exception ex){
            logger.error(ex.getMessage(), ex.getStackTrace());
            jsonObject.addProperty("error", "Exception occured! Check log");
        }

        return jsonObject.toString();

    }
    public String generateFileName(MultipartFile multiPart) {


        String uploadDir = System.getProperty("user.home") + "/Desktop/Images/";
        File f = new File(uploadDir);
        if (!f.exists()) {
            f.mkdir();


        }
        logger.info(uploadDir);

        return uploadDir + multiPart.getOriginalFilename().replace(" ", "_");
    }

    public boolean fileCheck(String filetype) {

        return( filetype.equals(JPEG) || filetype.equals(JPG) || filetype.equals(PNG));


    }



}
