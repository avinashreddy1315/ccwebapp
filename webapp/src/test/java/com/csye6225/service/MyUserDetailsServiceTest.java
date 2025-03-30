//package com.csye6225.service;
//
//
//import com.csye6225.models.User;
//import com.csye6225.repository.UserRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.context.junit4.SpringRunner;
//import static junit.framework.TestCase.assertEquals;
//
//@RunWith(SpringRunner.class)
//public class MyUserDetailsServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    private User user;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        this.user = new User("b@b.com","password");
//        Mockito.when(userRepository.findByEmailAddress("b@b.com")).thenReturn(this.user);
//    }
//
//    @Test
//    public void TestfindByEmailAddressPositive() {
//        User u = userRepository.findByEmailAddress("b@b.com");
//        assertEquals(u.getEmailAddress(), "b@b.com");
//    }
//}
