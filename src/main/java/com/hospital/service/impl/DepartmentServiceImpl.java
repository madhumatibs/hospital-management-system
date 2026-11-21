package com.hospital.service.impl;
import com.hospital.entity.Department; import com.hospital.exception.ResourceNotFoundException; import com.hospital.repository.DepartmentRepository; import com.hospital.repository.DoctorRepository; import com.hospital.service.DepartmentService; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class DepartmentServiceImpl extends AbstractCrudService<Department> implements DepartmentService {
 private final DepartmentRepository departments; private final DoctorRepository doctors;
 public DepartmentServiceImpl(DepartmentRepository departments,DoctorRepository doctors){super(departments);this.departments=departments;this.doctors=doctors;}
 @Override @Transactional public Department create(Department d){d.setDepartmentId(null);d.setDepartmentName(d.getDepartmentName()==null?null:d.getDepartmentName().trim());checkUnique(d.getDepartmentName(),null);return departments.save(d);}
 @Override @Transactional public Department update(Integer id,Department d){findById(id);checkUnique(d.getDepartmentName(),id);d.setDepartmentId(id);return departments.save(d);}
 @Override @Transactional public void delete(Integer id){departments.lockById(id).orElseThrow(()->new com.hospital.exception.ResourceNotFoundException("Department not found: "+id));if(doctors.countByDepartmentId(id)>0)throw new IllegalArgumentException("Department cannot be deleted while doctors are assigned to it.");departments.deleteById(id);}
 private void checkUnique(String name,Integer id){if(name==null||name.isBlank())throw new IllegalArgumentException("Department name is required.");departments.findByDepartmentNameIgnoreCase(name.trim()).filter(existing->!existing.getDepartmentId().equals(id)).ifPresent(existing->{throw new IllegalArgumentException("Department name already exists.");});}
}
