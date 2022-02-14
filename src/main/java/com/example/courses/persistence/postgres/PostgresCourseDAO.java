package com.example.courses.persistence.postgres;

import com.example.courses.persistence.CourseDAO;
import com.example.courses.persistence.DAOFactory;
import com.example.courses.persistence.entity.Course;
import com.example.courses.persistence.entity.CourseStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostgresCourseDAO implements CourseDAO {

    @Override
    public long saveCourse(Connection connection, Course course) throws SQLException {
        long insertedCourseId;
        PreparedStatement statement = null;
        ResultSet generatedKey = null;

        try {
            statement = connection.prepareStatement(
                    CourseDAOConstants.INSERT_COURSE,
                    Statement.RETURN_GENERATED_KEYS
            );

            setCourseProperties(course, statement);
            statement.executeUpdate();

            generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()) {
                insertedCourseId = generatedKey.getLong(1);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(generatedKey);
            DAOFactory.closeResource(statement);
        }

        return insertedCourseId;
    }

    @Override
    public void deleteCourseById(Connection connection, long id) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(CourseDAOConstants.DELETE_COURSE_BY_ID);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public void updateCourse(Connection connection, Course course) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(CourseDAOConstants.UPDATE_COURSE_BY_ID);
            setCourseProperties(course, statement);
            statement.setLong(10, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(statement);
        }
    }

    @Override
    public Course findCourse(Connection connection, long courseId) throws SQLException {
        Course course = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(CourseDAOConstants.SELECT_COURSE_BY_ID);
            statement.setLong(1, courseId);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                course = parseCourse(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return course;
    }

    @Override
    public List<Course> findAll(Connection connection) throws SQLException {
        List<Course> courseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(CourseDAOConstants.SELECT_ALL);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                courseList.add(parseCourse(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return courseList;
    }

    @Override
    public List<Course> findAvailable(Connection connection) throws SQLException {
        List<Course> courseList = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(CourseDAOConstants.SELECT_AVAILABLE);

            while (resultSet.next()) {
                courseList.add(parseCourse(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return courseList;
    }

    @Override
    public List<Course> findCoursesBySearchQuery(Connection connection, String query) throws SQLException {
        List<Course> courseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        query = "%" + query + "%";

        try {
            statement = connection.prepareStatement(CourseDAOConstants.SELECT_COURSES_BY_SEARCH_REQUEST);
            statement.setString(1, query);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.add(parseCourse(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return courseList;
    }

    @Override
    public List<Course> findAvailableCoursesBySearchQuery(Connection connection, String query) throws SQLException {
        List<Course> courseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        query = "%" + query + "%";

        try {
            statement = connection.prepareStatement(CourseDAOConstants.SELECT_AVAILABLE_COURSES_BY_SEARCH_REQUEST);
            statement.setString(1, query);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.add(parseCourse(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return courseList;
    }

    @Override
    public List<Course> findCoursesByTeacherId(Connection connection, long teacherId) throws SQLException {
        List<Course> courseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(CourseDAOConstants.SELECT_COURSES_BY_TEACHER);
            statement.setLong(1, teacherId);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courseList.add(parseCourse(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return courseList;
    }

    @Override
    public List<Course> findCoursesByLanguageId(Connection connection, long languageId) throws SQLException {
        List<Course> courseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(CourseDAOConstants.SELECT_COURSES_BY_LANGUAGE);
            statement.setLong(1, languageId);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                courseList.add(parseCourse(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            DAOFactory.closeResource(resultSet);
            DAOFactory.closeResource(statement);
        }

        return courseList;
    }

    private void setCourseProperties(Course course, PreparedStatement statement) throws SQLException {
        statement.setLong(1, course.getTeacherId());
        statement.setLong(2, course.getLanguageId());
        statement.setLong(3, course.getSubjectId());
        statement.setString(4, course.getTitle());
        statement.setString(5, course.getDescription());
        statement.setTimestamp(6, Timestamp.valueOf(course.getStartDate()));
        statement.setTimestamp(7, Timestamp.valueOf(course.getEndDate()));
        statement.setInt(8, course.getMaxScore());
        statement.setString(9, course.getImageUrl());
    }

    private Course parseCourse(ResultSet resultSet) throws SQLException {
        Course course = new Course();

        course.setId(resultSet.getLong(CourseDAOConstants.COURSE_ID));
        course.setTeacherId(resultSet.getLong(CourseDAOConstants.COURSE_TEACHER_ID));
        course.setLanguageId(resultSet.getLong(CourseDAOConstants.COURSE_LANGUAGE_ID));
        course.setTitle(resultSet.getString(CourseDAOConstants.COURSE_TITLE));
        course.setSubjectId(resultSet.getLong(CourseDAOConstants.COURSE_SUBJECT_ID));
        course.setDescription(resultSet.getString(CourseDAOConstants.COURSE_DESCRIPTION));

        LocalDateTime startDate = resultSet.getTimestamp(CourseDAOConstants.COURSE_START_DATE).toLocalDateTime();
        course.setStartDate(startDate);

        LocalDateTime endDate = resultSet.getTimestamp(CourseDAOConstants.COURSE_END_DATE).toLocalDateTime();
        course.setEndDate(endDate);

        course.setMaxScore(resultSet.getInt(CourseDAOConstants.COURSE_MAX_SCORE));
        course.setImageUrl(resultSet.getString(CourseDAOConstants.COURSE_IMAGE_URL));

        course.setCourseStatus(parseCourseStatus(startDate, endDate));

        return course;
    }

    private CourseStatus parseCourseStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();

        if(startDate.isAfter(now)){
            return CourseStatus.NOT_STARTED;
        } else if(endDate.isBefore(now)){
            return CourseStatus.COMPLETED;
        } else {
            return CourseStatus.IN_PROGRESS;
        }
    }

    private static class CourseDAOConstants {
        static final String TABLE_COURSE = "course";

        static final String COURSE_ID = "id";
        static final String COURSE_TEACHER_ID = "teacher_id";
        static final String COURSE_LANGUAGE_ID = "language_id";
        static final String COURSE_TITLE = "title";
        static final String COURSE_SUBJECT_ID = "subject_id";
        static final String COURSE_DESCRIPTION = "description";
        static final String COURSE_START_DATE = "start_date";
        static final String COURSE_END_DATE = "end_date";
        static final String COURSE_MAX_SCORE = "max_score";
        static final String COURSE_IMAGE_URL = "image_url";

        static final String INSERT_COURSE =
                "INSERT INTO " +
                        TABLE_COURSE +
                        "(" +
                            COURSE_TEACHER_ID + ", " +
                            COURSE_LANGUAGE_ID + ", " +
                            COURSE_SUBJECT_ID + ", " +
                            COURSE_TITLE + ", " +
                            COURSE_DESCRIPTION + ", " +
                            COURSE_START_DATE + ", " +
                            COURSE_END_DATE + ", " +
                            COURSE_MAX_SCORE + ", " +
                            COURSE_IMAGE_URL +
                        ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        static final String UPDATE_COURSE_BY_ID =
                "UPDATE " + TABLE_COURSE + " " +
                        "SET " +
                        COURSE_TEACHER_ID + " = ?, " +
                        COURSE_LANGUAGE_ID + " = ?, " +
                        COURSE_SUBJECT_ID + " = ?, " +
                        COURSE_TITLE + " = ?, " +
                        COURSE_DESCRIPTION + " = ?, " +
                        COURSE_START_DATE + " = ?, " +
                        COURSE_END_DATE + " = ?, " +
                        COURSE_MAX_SCORE + " = ?, " +
                        COURSE_IMAGE_URL + " = ? " +
                        "WHERE " + COURSE_ID + " = ?;";

        static final String DELETE_COURSE_BY_ID =
                "DELETE FROM " +
                        TABLE_COURSE + " " +
                        "WHERE " + COURSE_ID + " = " + "?;";

        static final String SELECT_COURSE_PROPERTIES =
                COURSE_ID + ", " +
                        "c." + COURSE_TEACHER_ID + ", " +
                        "c." + COURSE_LANGUAGE_ID + ", " +
                        "c." + COURSE_SUBJECT_ID + ", " +
                        "c." + COURSE_TITLE + ", " +
                        "c." + COURSE_DESCRIPTION + ", " +
                        "c." + COURSE_START_DATE + ", " +
                        "c." + COURSE_END_DATE + ", " +
                        "c." + COURSE_MAX_SCORE + ", " +
                        "c." + COURSE_IMAGE_URL;

        static final String SELECT_COURSE_BY_ID =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_COURSE + " AS c " +
                        "WHERE " + COURSE_ID + " = ?;";

        static final String SELECT_COURSES_BY_SEARCH_REQUEST =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                "FROM " + TABLE_COURSE + " AS c " +
                "WHERE " + COURSE_TITLE + " ilike " + "?;";

        static final String SELECT_AVAILABLE_COURSES_BY_SEARCH_REQUEST =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                "FROM " + TABLE_COURSE + " AS c " +
                "WHERE " + COURSE_START_DATE + " > NOW() " +
                "AND " + COURSE_TITLE + " ilike " + "?;";

        static final String SELECT_COURSES_BY_LANGUAGE =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_COURSE + " AS c " +
                        "WHERE " + COURSE_LANGUAGE_ID + " = " + "?;";

        static final String SELECT_COURSES_BY_TEACHER =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_COURSE + " AS c " +
                        "WHERE " + COURSE_TEACHER_ID + " = " + "?;";

        static final String SELECT_ALL =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_COURSE + " AS c;";

        static final String SELECT_AVAILABLE =
                "SELECT " +
                        SELECT_COURSE_PROPERTIES + " " +
                        "FROM " + TABLE_COURSE + " AS c " +
                        "WHERE " + COURSE_START_DATE + " > NOW()";
    }
}
