package Service;

import Dao.ReimbursementDAO;
import Dao.UsersDAO;
import Model.Reimbursement;
import Model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReimbursementServiceImplTest {

    private ReimbursementDAO mockReimbursementDao;
    private UsersDAO mockUserDao;
    private ReimbursementServiceImpl service;

    @BeforeEach
    void setUp() {
        mockReimbursementDao = mock(ReimbursementDAO.class);
        mockUserDao = mock(UsersDAO.class);
        service = new ReimbursementServiceImpl(mockReimbursementDao, mockUserDao);
    }

    @Test
    void create_withValidData_forcesPendingStatusAndNullResolver() {
        Reimbursement r = new Reimbursement(0, "APPROVED", new BigDecimal("50.00"), "Lunch", "FOOD", 1, 2);
        // status/resolver set on purpose here to prove the service overrides them
        when(mockReimbursementDao.create(any())).thenAnswer(inv -> inv.getArgument(0));

        Reimbursement result = service.create(r);

        assertEquals("PENDING", result.getStatus());
        assertNull(result.getResolver_id());
    }

    @Test
    void create_withZeroAmount_throwsException() {
        Reimbursement r = new Reimbursement(0, "PENDING", BigDecimal.ZERO, "Lunch", "FOOD", null, 2);
        assertThrows(IllegalArgumentException.class, () -> service.create(r));
    }

    @Test
    void create_withAmountOverCeiling_throwsException() {
        Reimbursement r = new Reimbursement(0, "PENDING", new BigDecimal("10000.01"), "Big expense", "OTHER", null, 2);
        assertThrows(IllegalArgumentException.class, () -> service.create(r));
    }

    @Test
    void create_withInvalidType_throwsException() {
        Reimbursement r = new Reimbursement(0, "PENDING", new BigDecimal("50.00"), "Lunch", "MEALS", null, 2);
        assertThrows(IllegalArgumentException.class, () -> service.create(r));
    }

    @Test
    void create_withDescriptionTooLong_throwsException() {
        // 501 chars, one over the limit
        String longDesc = "a".repeat(501);
        Reimbursement r = new Reimbursement(0, "PENDING", new BigDecimal("50.00"), longDesc, "FOOD", null, 2);
        assertThrows(IllegalArgumentException.class, () -> service.create(r));
    }

    @Test
    void create_withInvalidAuthorId_throwsException() {
        Reimbursement r = new Reimbursement(0, "PENDING", new BigDecimal("50.00"), "Lunch", "FOOD", null, 0);
        assertThrows(IllegalArgumentException.class, () -> service.create(r));
    }

    @Test
    void update_onResolvedReimbursement_throwsException() {
        // this is the immutability rule, already resolved records can't be edited
        Reimbursement existing = new Reimbursement(1, "APPROVED", new BigDecimal("50.00"), "Lunch", "FOOD", 4, 2);
        when(mockReimbursementDao.findById(1)).thenReturn(existing);

        Reimbursement edit = new Reimbursement(1, "PENDING", new BigDecimal("60.00"), "Updated lunch", "FOOD", null, 2);

        assertThrows(IllegalArgumentException.class, () -> service.update(edit));
        verify(mockReimbursementDao, never()).update(any());
    }

    @Test
    void update_onPendingReimbursement_callsDao() {
        Reimbursement existing = new Reimbursement(1, "PENDING", new BigDecimal("50.00"), "Lunch", "FOOD", null, 2);
        when(mockReimbursementDao.findById(1)).thenReturn(existing);

        Reimbursement edit = new Reimbursement(1, "PENDING", new BigDecimal("60.00"), "Updated lunch", "FOOD", null, 2);

        service.update(edit);

        verify(mockReimbursementDao).update(edit);
    }

    @Test
    void resolve_onAlreadyResolvedReimbursement_throwsException() {
        Reimbursement existing = new Reimbursement(1, "DENIED", new BigDecimal("50.00"), "Lunch", "FOOD", 4, 2);
        when(mockReimbursementDao.findById(1)).thenReturn(existing);

        Reimbursement resolveAttempt = new Reimbursement(1, "APPROVED", null, null, null, 1, 2);

        assertThrows(IllegalArgumentException.class, () -> service.resolve(resolveAttempt));
        verify(mockReimbursementDao, never()).resolve(any());
    }

    @Test
    void resolve_withNonManagerResolver_throwsException() {
        Reimbursement existing = new Reimbursement(1, "PENDING", new BigDecimal("50.00"), "Lunch", "FOOD", null, 2);
        when(mockReimbursementDao.findById(1)).thenReturn(existing);

        Users notAManager = new Users(3, "bobk", "hash", false, "Bob", "Kim", 2);
        when(mockUserDao.findById(3)).thenReturn(notAManager);

        Reimbursement resolveAttempt = new Reimbursement(1, "APPROVED", null, null, null, 3, 2);

        assertThrows(IllegalArgumentException.class, () -> service.resolve(resolveAttempt));
    }

    @Test
    void resolve_withValidManager_callsDao() {
        Reimbursement existing = new Reimbursement(1, "PENDING", new BigDecimal("50.00"), "Lunch", "FOOD", null, 2);
        when(mockReimbursementDao.findById(1)).thenReturn(existing);

        Users manager = new Users(1, "johnvt", "hash", true, "John", "Vermont", 1);
        when(mockUserDao.findById(1)).thenReturn(manager);

        Reimbursement resolveAttempt = new Reimbursement(1, "APPROVED", null, null, null, 1, 2);

        service.resolve(resolveAttempt);

        verify(mockReimbursementDao).resolve(resolveAttempt);
    }

    @Test
    void resolve_withInvalidStatus_throwsException() {
        Reimbursement existing = new Reimbursement(1, "PENDING", new BigDecimal("50.00"), "Lunch", "FOOD", null, 2);
        when(mockReimbursementDao.findById(1)).thenReturn(existing);

        // "RESOLVED" isn't a real status, only APPROVED/DENIED are valid
        Reimbursement resolveAttempt = new Reimbursement(1, "RESOLVED", null, null, null, 1, 2);

        assertThrows(IllegalArgumentException.class, () -> service.resolve(resolveAttempt));
    }

    @Test
    void findAllByStatusAndDepartment_withInvalidStatus_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findAllByStatusAndDepartment("MAYBE", 1));
    }

    @Test
    void findAllByStatusAndDepartment_withInvalidDepartmentId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findAllByStatusAndDepartment("PENDING", 0));
    }
}