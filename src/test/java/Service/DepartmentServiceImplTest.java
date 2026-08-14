package Service;

import Dao.DepartmentDAO;
import Model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartmentServiceImplTest {

    private DepartmentDAO mockDao;
    private DepartmentServiceImpl service;

    // fresh fake DAO before each test so nothing carries over between them
    @BeforeEach
    void setUp() {
        mockDao = mock(DepartmentDAO.class);
        service = new DepartmentServiceImpl(mockDao);
    }

    @Test
    void create_withValidName_callsDaoCreate() {
        Department dept = new Department(0, "Engineering");
        when(mockDao.create(dept)).thenReturn(dept);

        Department result = service.create(dept);

        assertEquals("Engineering", result.getName());
        verify(mockDao, times(1)).create(dept); // make sure it actually hit the dao once
    }

    @Test
    void create_withNullName_throwsException() {
        Department dept = new Department(0, null);

        assertThrows(IllegalArgumentException.class, () -> service.create(dept));
        verify(mockDao, never()).create(any()); // should never reach the dao
    }

    @Test
    void create_withEmptyName_throwsException() {
        Department dept = new Department(0, "");

        assertThrows(IllegalArgumentException.class, () -> service.create(dept));
        verify(mockDao, never()).create(any());
    }

    @Test
    void create_withWhitespaceOnlyName_throwsException() {
        // just spaces, should get caught by the .trim() check in the service
        Department dept = new Department(0, "   ");

        assertThrows(IllegalArgumentException.class, () -> service.create(dept));
        verify(mockDao, never()).create(any());
    }

    @Test
    void findById_withValidId_callsDao() {
        Department dept = new Department(1, "Sales");
        when(mockDao.findById(1)).thenReturn(dept);

        Department result = service.findById(1);

        assertEquals("Sales", result.getName());
        verify(mockDao).findById(1);
    }

    @Test
    void findById_withInvalidId_throwsException() {
        // 0 and negative should both get rejected before ever hitting the dao
        assertThrows(IllegalArgumentException.class, () -> service.findById(0));
        assertThrows(IllegalArgumentException.class, () -> service.findById(-5));
        verify(mockDao, never()).findById(anyInt());
    }

    @Test
    void findAll_callsDao() {
        // no validation here, just confirming it's a straight passthrough
        service.findAll();
        verify(mockDao, times(1)).findAll();
    }
}