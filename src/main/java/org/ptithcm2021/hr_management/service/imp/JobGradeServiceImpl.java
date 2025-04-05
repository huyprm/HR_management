package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.JobGradeRequest;
import org.ptithcm2021.hr_management.dto.response.JobGradeResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.JobGradeMapper;
import org.ptithcm2021.hr_management.model.JobGrade;
import org.ptithcm2021.hr_management.model.Position;
import org.ptithcm2021.hr_management.repository.JobGradeRepository;
import org.ptithcm2021.hr_management.service.JobGradeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobGradeServiceImpl implements JobGradeService {
    private final JobGradeRepository jobGradeRepository;
    private final JobGradeMapper jobGradeMapper;


    @Override
    public JobGradeResponse createJobGrade(JobGradeRequest jobGradeRequest) {
        if (jobGradeRepository.existsByName(jobGradeRequest.getName()))
            throw new AppException(ErrorCode.JOB_GRADE_NAME_EXISTS);

        JobGrade jobGrade = jobGradeMapper.toJobGrade(jobGradeRequest);

        return jobGradeMapper.toJobGradeResponse(jobGradeRepository.save(jobGrade));
    }

    @Override
    public JobGradeResponse updateJobGrade(String id, JobGradeRequest jobGradeRequest) {
        if (jobGradeRepository.existsByName(jobGradeRequest.getName()))
            throw new AppException(ErrorCode.JOB_GRADE_NAME_EXISTS);

        JobGrade jobGrade = jobGradeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

        jobGradeMapper.updateJobGrade(jobGrade, jobGradeRequest);

        return jobGradeMapper.toJobGradeResponse(jobGradeRepository.save(jobGrade));
    }

    @Override
    public void deleteJobGrade(String id) {
        if (!jobGradeRepository.existsById(id))
            throw new AppException(ErrorCode.JOB_GRADE_NOT_FOUND);

        try{
            jobGradeRepository.deleteById(id);
        }catch (Exception e){
            throw new AppException(ErrorCode.CANNOT_BE_DELETED);
        }
    }

    @Override
    public JobGradeResponse getJobGrade(String id) {
        JobGrade jobGrade = jobGradeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_GRADE_NOT_FOUND));

        return jobGradeMapper.toJobGradeResponse(jobGrade);
    }

    @Override
    public List<JobGradeResponse> getAllJobGrade() {
        return jobGradeRepository.findAll()
                .stream().map(jobGradeMapper::toJobGradeResponse).toList();
    }
}
