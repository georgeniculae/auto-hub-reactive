package com.autohubreactive.agency.service;

import com.autohubreactive.agency.entity.Employee;
import com.autohubreactive.agency.mapper.EmployeeMapper;
import com.autohubreactive.agency.repository.EmployeeRepository;
import com.autohubreactive.dto.agency.EmployeeRequest;
import com.autohubreactive.dto.agency.EmployeeResponse;
import com.autohubreactive.exception.AutoHubException;
import com.autohubreactive.exception.AutoHubNotFoundException;
import com.autohubreactive.lib.exceptionhandling.ExceptionUtil;
import com.autohubreactive.lib.util.MongoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchService branchService;
    private final EmployeeMapper employeeMapper;

    public Flux<EmployeeResponse> findAllEmployees() {
        return employeeRepository.findAll()
                .map(employeeMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding all employees: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Mono<EmployeeResponse> findEmployeeById(String id) {
        return findEntityById(id)
                .map(employeeMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding employee by id: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });

    }

    public Mono<EmployeeResponse> saveEmployee(EmployeeRequest employeeRequest) {
        return branchService.findEntityById(employeeRequest.workingBranchId())
                .flatMap(workingBranch -> {
                    Employee newEmployee = employeeMapper.getNewEmployee(employeeRequest, workingBranch);

                    return employeeRepository.save(newEmployee);
                })
                .map(employeeMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while saving employee: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<EmployeeResponse> updateEmployee(String id, EmployeeRequest updatedEmployeeRequest) {
        return Mono.zip(
                        findEntityById(id),
                        branchService.findEntityById(updatedEmployeeRequest.workingBranchId()),
                        (existingEmployee, workingBranch) -> employeeMapper.getUpdatedEmployee(existingEmployee, updatedEmployeeRequest, workingBranch)
                )
                .flatMap(employeeRepository::save)
                .map(employeeMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while updating employee: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Flux<EmployeeResponse> findEmployeesByBranchId(String id) {
        return employeeRepository.findAllEmployeesByBranchId(MongoUtil.getObjectId(id))
                .map(employeeMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding all employees ny branch id: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Flux<EmployeeResponse> findEmployeeByFilterInsensitiveCase(String filter) {
        return employeeRepository.findAllByFilterInsensitiveCase(filter)
                .switchIfEmpty(Mono.error(new AutoHubNotFoundException("Employee with filter: " + filter + " does not exist")))
                .map(employeeMapper::mapEntityToDto)
                .onErrorMap(e -> {
                    log.error("Error while finding employee by filter: {}", e.getMessage());

                    return ExceptionUtil.handleException(e);
                });
    }

    public Mono<Long> countEmployees() {
        return employeeRepository.count()
                .onErrorMap(e -> {
                    log.error("Error while counting employees: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Mono<Void> deleteEmployeeById(String id) {
        return employeeRepository.deleteById(MongoUtil.getObjectId(id))
                .onErrorMap(e -> {
                    log.error("Error while deleting employee: {}", e.getMessage());

                    return new AutoHubException(e.getMessage());
                });
    }

    public Mono<Employee> findEntityById(String id) {
        return employeeRepository.findById(MongoUtil.getObjectId(id))
                .switchIfEmpty(Mono.error(new AutoHubNotFoundException("Employee with id " + id + " does not exist")));
    }

}
