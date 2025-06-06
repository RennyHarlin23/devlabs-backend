package com.devlabs.devlabsbackend.batch.service

import com.devlabs.devlabsbackend.batch.domain.Batch
import com.devlabs.devlabsbackend.batch.domain.dto.BatchResponse
import com.devlabs.devlabsbackend.batch.repository.BatchRepository
import com.devlabs.devlabsbackend.core.exception.NotFoundException
import com.devlabs.devlabsbackend.semester.repository.SemesterRepository
import com.devlabs.devlabsbackend.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Year
import java.util.*

@Service
class BatchService(
    private val userRepository: UserRepository,
    private val batchRepository: BatchRepository,
    private val semesterRepository: SemesterRepository
) {

    fun addStudentsToBatch(batchId: UUID, studentId: List<UUID>) {
        val batch = batchRepository.findById(batchId).orElseThrow {
            NotFoundException("Could not find course with id $batchId")
        }
        val users = userRepository.findAllById(studentId)
        batch.students.addAll(users)
        batchRepository.save(batch)
    }

    fun removeStudentsFromBatch(batchId: UUID, studentId: List<UUID>) {
        val batch = batchRepository.findById(batchId).orElseThrow {
            NotFoundException("Could not find course with id $batchId")
        }
        val users = userRepository.findAllById(studentId)
        batch.students.removeAll(users)
        batchRepository.save(batch)
    }

    fun addSemestersToBatches(batchId: UUID, semesterId: List<UUID>) {
        val batch = batchRepository.findById(batchId).orElseThrow {
            NotFoundException("Could not find course with id $batchId")
        }
        val semesters = semesterRepository.findAllById(semesterId)
        batch.semester.addAll(semesters)
        batchRepository.save(batch)
    }

    fun removeSemestersFromBatch(batchId: UUID, semesterId: List<UUID>) {
        val batch = batchRepository.findById(batchId).orElseThrow {
            NotFoundException("Could not find course with id $batchId")
        }
        val semesters = semesterRepository.findAllById(semesterId)
        batch.semester.removeAll(semesters)
        batchRepository.save(batch)
    }

    fun addManagersToBatch(batchId: UUID, managerId: List<UUID>) {
        val batch = batchRepository.findById(batchId).orElseThrow {
            NotFoundException("Could not find course with id $batchId")
        }
        val managers = userRepository.findAllById(managerId)
        batch.managers.addAll(managers)
        batchRepository.save(batch)
    }

    fun removeManagersFromBatch(batchId: UUID, managerId: List<UUID>) {
        val batch = batchRepository.findById(batchId).orElseThrow {
            NotFoundException("Could not find course with id $batchId")
        }
        val managers = userRepository.findAllById(managerId)
        batch.managers.removeAll(managers)
        batchRepository.save(batch)
    }

    fun getAllBatches(): List<BatchResponse> {
        val batches = batchRepository.findAll()
        return batches.map { it.toBatchResponse() }
    }

    fun searchBatches(query: String): List<BatchResponse> {
        return batchRepository.findByNameOrYearContainingIgnoreCase(query).map{
            batch -> batch.toBatchResponse()
        }
    }
}

fun Batch.toBatchResponse(): BatchResponse {
    return BatchResponse(
        id = this.id,
        name = this.name,
        batch = this.batch,
        department = this.department,
        section = this.section,
        isActive = this.isActive,
    )
}

