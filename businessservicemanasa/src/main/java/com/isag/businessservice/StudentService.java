/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.StudentDTO;
import com.isag.entity.Student;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface StudentService {

    public APIStudentDataDTO ResetPassword(APIStudentDataDTO aPIStudentDataDTO);

    public StudentDTO authenticateUser(String userName, String password, Integer branchId, Integer companyId);

    public APIStudentDataDTO save(APIStudentDataDTO aPIStudentDataDTO);

    public List<StudentDTO> getAllStudentsListByCompanyId(Integer companyId);

    public List<StudentDTO> getAllStudentsListByCompanyIdAndRemType(Integer companyId, Integer remType);

    public List<StudentDTO> getAllStudentsListByCompanyId(Integer companyId, Integer branchId);

    public List<StudentDTO> getAllStudentsListByParentANDUserLogin(Integer studentId);

    public List<Student> getAllStudentListByCompanyId(Integer companyId);

    public List<Student> getAllStudentsListByBranchId(Integer branchId);

    public List<StudentDTO> getStudentbySchoolStagesGuideId(Integer schoolStagesId);

    public List<APIStudentDataDTO> getStudentbyBranchId(Integer branchId, Integer schoolStagesId, Integer id);

    public StudentDTO findStudentId(Integer id);

    public StudentDTO addStudent(StudentDTO student);

    public List<Student> findStudentByCode(Integer companyId, Integer code);

    public StudentDTO findStudent(Integer departmentId);

    public StudentDTO findStudentByMobile(String mobile, Integer branchId);

    public StudentDTO updateStudent(StudentDTO student);

    public StudentDTO updateStudentMacId(StudentDTO studentDTO);

    public void deleteStudent(StudentDTO student);

    public void deleteStudent(Integer student);

    public StudentDTO addStudent(StudentDTO student, IgmaUser isagUser);

    public void deleteIdfromFellowsQualifications(Integer studentId);

    public List<StudentDTO> getAllStudentsListByBranchIdAndHrSalaryStructure(Integer branchId);

    public List<StudentDTO> getAllStudentsListByParentId(Integer parentId);

    public Boolean getAllStudentsIsSuperVisor(Integer studentId);

    public List<StudentDTO> getAllStudentsListByParentAndchields(Integer branchId, Integer parentId);

    public List<Integer> getAllStudentsListByParentAndchield(Integer branchId, Integer parentId);

    public List<StudentDTO> getAllStudentsListByCompanyIdAndBranchId(Integer companyId, Integer branchId);

    public List<StudentDTO> getAllStudentsListByCompanyIdByType(Integer branchId, Integer remType);

    public List<StudentDTO> getAllStudentsListByRemCompanyId(Integer remCompany);

    public List<StudentDTO> getAllStudentsListByRemPointsId(Integer remPoints, Integer remTrafficId);

}
