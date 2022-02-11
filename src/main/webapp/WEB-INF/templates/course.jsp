<%@ page import="com.example.courses.persistence.entity.Role" %>
<%@ page import="com.example.courses.persistence.entity.CourseStatus" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/courses/course.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/table.css">
</head>
<body>
<jsp:include page="/WEB-INF/templates/header.jsp"/>

<main>
    <c:set var="courseDTO" value="${requestScope.course}"/>
    <c:set var="course" value="${courseDTO.getCourse()}"/>
    <c:set var="teacher" value="${courseDTO.getTeacher()}"/>
    <c:set var="students" value="${courseDTO.getStudents()}"/>

    <div class="content-block">
        <div class="container">
            <div class="content">
                <div class="info-block">
                    <h1 class="course-name">${course.getTitle()}</h1>
                    <h3 class="course-subject">${course.getSubject()}</h3>
                    <p class="course-description">
                        ${course.getDescription()}
                    </p>
                    <p class="teacher">Teacher:
                        <a href="${pageContext.request.contextPath}/user?user_id=${teacher.getId()}">
                            <span>${teacher.getFullName()}</span>
                        </a>
                    </p>

                    <c:if test="${sessionScope.user == null || sessionScope.user.getRole().equals(Role.STUDENT)}">
                        <div class="enroll-block">
                            <c:if test="${requestScope.student_course == null}">
                                <form action="${pageContext.request.contextPath}/enroll?course_id=${course.getId()}"
                                      method="post">
                                    <button type="submit" class="enroll-btn">
                                        <span>Enroll</span>
                                        <span>Starts:${course.getStartDate().toLocalDate()}</span>
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${requestScope.student_course != null}">
                                <div class="enrolled">
                                    <span>Enrolled</span>
                                </div>
                            </c:if>
                            <div class="enrolled-count">
                                <span class="enrolled-span">Number of students: </span>
                                <span>${students.size()}</span>
                            </div>
                        </div>
                    </c:if>
                </div>

                <div class="image-block">
                    <div class="image-wrapper">
                        <img src="${pageContext.request.contextPath}/static/images/default.jpeg" alt=""/>
                    </div>
                </div>
            </div>

            <c:if test="${sessionScope.user != null && sessionScope.user.getRole().equals(Role.TEACHER)}">
                <div class="students-block">
                    <h1>Students</h1>
                    <c:if test="${!students.isEmpty()}">
                        <form action="${pageContext.request.contextPath}/teacher/course/score?course_id=${course.getId()}"
                              method="post">

                            <table class="students-table">
                                <tr>
                                    <th class="first-name-header">First name</th>
                                    <th class="last-name-header">Last name</th>
                                    <th class="email-header">Email</th>
                                    <th class="score-header">Score</th>
                                </tr>
                                <c:forEach var="student" items="${students}">
                                    <tr>
                                        <td class="first-name">
                                            <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                                    ${student.getFirstName()}
                                            </a>
                                        </td>
                                        <td class="last-name">
                                            <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                                    ${student.getLastName()}
                                            </a>
                                        </td>
                                        <td class="email">
                                            <a href="${pageContext.request.contextPath}/user?user_id=${student.getId()}">
                                                    ${student.getEmail()}
                                            </a>
                                        </td>

                                        <td class="student-score">
                                            <input type="number" min="0" max="${course.getMaxScore()}"
                                                   placeholder="${requestScope.scores.getOrDefault(student.getId(), 0)}"
                                                   name="score_${student.getId()}">
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <button class="btn" type="submit">Save scores</button>
                        </form>
                    </c:if>
                    <c:if test="${students.isEmpty()}">
                        <div class="no-students">
                            <h3>No one takes this course</h3>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>

    <c:if test="${sessionScope.user != null && requestScope.student_course != null}">
        <div class="action-block">
            <div class="container">
                <div class="status-box">
                    <h3>Status:</h3>
                    <h3>${course.getCourseStatus().getStatus()}</h3>
                </div>

                <c:if test="${sessionScope.user.getRole().equals(Role.STUDENT) && course.getCourseStatus().equals(CourseStatus.COMPLETED)}">
                    <div class="score-box">
                        <h3>Score: </h3>
                        <h3>${requestScope.student_course.getScore()}/${course.getMaxScore()}</h3>
                    </div>
                </c:if>

                <c:if test="${sessionScope.user.getRole().equals(Role.ADMIN)}">
                    <div class="manage-box">
                        <button class="manage-btn edit-btn">
                            <a href="${pageContext.request.contextPath}/admin/course/edit?course_id=${course.getId()}">Edit</a>
                        </button>
                        <button class="manage-btn delete-btn">
                            <a href="${pageContext.request.contextPath}/admin/course/delete?course_id=${course.getId()}">Delete</a>
                        </button>
                    </div>
                </c:if>

            </div>
        </div>
    </c:if>
</main>
</body>
</html>