package com.devlabs.devlabsbackend.semester.service

import com.devlabs.devlabsbackend.batch.repository.BatchRepository
import com.devlabs.devlabsbackend.core.exception.NotFoundException
import com.devlabs.devlabsbackend.course.repository.CourseRepository
import com.devlabs.devlabsbackend.semester.domain.DTO.SemesterResponse
import com.devlabs.devlabsbackend.semester.domain.Semester
import com.devlabs.devlabsbackend.semester.repository.SemesterRepository
import com.devlabs.devlabsbackend.user.domain.DTO.UserResponse
import com.devlabs.devlabsbackend.user.repository.UserRepository
import com.devlabs.devlabsbackend.user.service.toUserResponse
import jakarta.persistence.Query
import org.springframework.stereotype.Service
import java.util.*

@Service
class SemesterService
    (
    val semesterRepository: SemesterRepository,
    val userRepository: UserRepository,
    private val courseRepository: CourseRepository,
    private val batchRepository: BatchRepository
) {
    fun assignManagersToSemester(semesterId: UUID, managersId: List<UUID>) {
        val semester = semesterRepository.findById(semesterId).orElseThrow {
            NotFoundException("Semester $semesterId not found")
        }
        val managers = userRepository.findAllById(managersId);
        if (managers.size != managersId.size) {
            throw NotFoundException("Some managers could not be found")
        }
        semester.managers.addAll(managers)
        semesterRepository.save(semester)
    }

    fun removeManagersFromSemester(semesterId: UUID, managersId: List<UUID>) {
        val semester = semesterRepository.findById(semesterId).orElseThrow {
            NotFoundException("Semester $semesterId not found")
        }
        val managers = userRepository.findAllById(managersId)
        if (managers.size != managersId.size) {
            throw NotFoundException("Some managers could not be found")
        }
        semester.managers.removeAll(managers)
        semesterRepository.save(semester)
    }

    fun addCourseToSemester(semesterId: UUID, courseId: List<UUID>) {
        val semester = semesterRepository.findById(semesterId).orElseThrow {
            NotFoundException("Semester with id $semesterId not found")
        }
        val courses = courseRepository.findAllById(courseId);
        semester.courses.addAll(courses)
        semesterRepository.save(semester)
    }

    fun removeCourseFromSemester(semesterId: UUID, courseId: List<UUID>) {
        val semester = semesterRepository.findById(semesterId).orElseThrow {
            NotFoundException("Semester with id $semesterId not found")
        }
        val courses = courseRepository.findAllById(courseId);
        semester.courses.removeAll(courses)
        semesterRepository.save(semester)
    }

    fun getAllSemester(): List<SemesterResponse> {
        return semesterRepository.findAll().map { it.toSemesterResponse() }
    }

    fun searchSemester(query: String): List<SemesterResponse> {
        return semesterRepository.findByNameOrYearContainingIgnoreCase(query).map { user -> user.toSemesterResponse() }
    }

}

fun Semester.toSemesterResponse(): SemesterResponse {
    return SemesterResponse(
        id = this.id!!,
        name = this.name,
        year = this.year,
        isActive = this.isActive
    )
}
