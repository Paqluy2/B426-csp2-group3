package com.joysistvi.employeemanagement.repository;

import com.joysistvi.employeemanagement.model.Leave;

import java.util.List;

public interface LeaveRepository {
    List<Leave> findAllActiveLeaves();
    List<Leave> findAllArchivedLeaves();
    List<Leave> findPendingLeaves();
    List<Leave> findLeavesByEmployeeId(int employeeId);
    boolean addLeave(Leave leave);
    boolean updateLeave(Leave leave);
    boolean approveLeave(Leave leave);
    boolean rejectLeave(Leave leave);
    boolean archiveLeave(Leave leave);
    boolean deleteArchivedLeave(Leave leave);
    List<Leave> searchLeavesByEmployeeName(String name);
}