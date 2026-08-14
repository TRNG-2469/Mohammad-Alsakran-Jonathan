package Service;

import Dao.UsersDAO;
import Model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {

    private UsersDAO mockDao;
    private UsersServiceImpl service;

    @BeforeEach
    void setUp() {
        mockDao = mock(UsersDAO.class);
        service = new UsersServiceImpl(mockDao);
    }

    @Test
    void create_withValidUser_hashesPasswordAndForcesEmployeeRole() {
        Users user = new Users(0, "newuser", "password123", true, "New", "User", 1);
        // role is deliberately true here to prove the service overrides it to false
        when(mockDao.findByUsername("newuser")).thenReturn(null);
        when(mockDao.create(any())).thenAnswer(inv -> inv.getArgument(0));

        Users result = service.create(user);

        assertFalse(result.isRole()); // can't self-assign manager on registration
        assertNotEquals("password123", result.getPassword()); // should be hashed, not raw
        assertTrue(BCrypt.checkpw("password123", result.getPassword()));
    }

    @Test
    void create_withMissingFirstName_throwsException() {
        Users user = new Users(0, "newuser", "password123", false, null, "User", 1);
        assertThrows(IllegalArgumentException.class, () -> service.create(user));
    }

    @Test
    void create_withShortUsername_throwsException() {
        Users user = new Users(0, "ab", "password123", false, "New", "User", 1);
        assertThrows(IllegalArgumentException.class, () -> service.create(user));
    }

    @Test
    void create_withShortPassword_throwsException() {
        Users user = new Users(0, "newuser", "short", false, "New", "User", 1);
        assertThrows(IllegalArgumentException.class, () -> service.create(user));
    }

    @Test
    void create_withDuplicateUsername_throwsException() {
        Users existing = new Users(1, "taken", "hash", false, "Existing", "User", 1);
        Users user = new Users(0, "taken", "password123", false, "New", "User", 1);
        when(mockDao.findByUsername("taken")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class, () -> service.create(user));
        verify(mockDao, never()).create(any()); // should never reach the dao
    }

    @Test
    void logIn_withCorrectPassword_returnsUserWithNullPassword() {
        String hashed = BCrypt.hashpw("password123", BCrypt.gensalt());
        Users stored = new Users(1, "johnvt", hashed, true, "John", "Vermont", 1);
        when(mockDao.findByUsername("johnvt")).thenReturn(stored);

        Users result = service.logIn("johnvt", "password123");

        assertNotNull(result);
        assertNull(result.getPassword()); // password should never leak back out
    }

    @Test
    void logIn_withWrongPassword_returnsNull() {
        String hashed = BCrypt.hashpw("password123", BCrypt.gensalt());
        Users stored = new Users(1, "johnvt", hashed, true, "John", "Vermont", 1);
        when(mockDao.findByUsername("johnvt")).thenReturn(stored);

        Users result = service.logIn("johnvt", "wrongpassword");

        assertNull(result);
    }

    @Test
    void logIn_withUnknownUsername_returnsNull() {
        when(mockDao.findByUsername("ghost")).thenReturn(null);

        Users result = service.logIn("ghost", "whatever");

        assertNull(result);
    }

    @Test
    void findById_withInvalidId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(0));
    }

    @Test
    void updateRole_withUnknownUser_throwsException() {
        when(mockDao.findById(99)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> service.updateRole(99, true));
    }

    @Test
    void updateRole_withValidUser_callsDao() {
        Users user = new Users(1, "johnvt", "hash", false, "John", "Vermont", 1);
        when(mockDao.findById(1)).thenReturn(user);

        service.updateRole(1, true);

        verify(mockDao).updateRole(1, true);
    }
}