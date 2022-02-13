package com.example.courses.persistence.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Course {
    long id;
    long teacherId;
    long languageId;
    long subjectId;
    String title;
    String description;
    int maxScore;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String imageUrl;
    CourseStatus courseStatus;

    public Course() {}

    public Course(long id, long teacherId, long languageId, long subjectId, String title, String description, int maxScore, LocalDateTime startDate, LocalDateTime endDate, String imageUrl, CourseStatus courseStatus) {
        this.id = id;
        this.teacherId = teacherId;
        this.languageId = languageId;
        this.title = title;
        this.subjectId = subjectId;
        this.description = description;
        this.maxScore = maxScore;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.courseStatus = courseStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", teacherId=" + teacherId +
                ", languageId=" + languageId +
                ", subjectId='" + subjectId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", maxScore=" + maxScore +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", imageUrl='" + imageUrl + '\'' +
                ", courseStatus=" + courseStatus +
                '}';
    }
}
