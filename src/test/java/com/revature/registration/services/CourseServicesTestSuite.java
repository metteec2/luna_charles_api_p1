package com.revature.registration.services;

import com.revature.registration.models.Course;
import com.revature.registration.models.Faculty;
import com.revature.registration.models.Student;
import com.revature.registration.repositories.CourseRepository;
import com.revature.registration.util.exceptions.DataSourceException;
import com.revature.registration.util.exceptions.InvalidInformationException;
import org.junit.*;

import java.util.List;

import static org.mockito.Mockito.*;

public class CourseServicesTestSuite {
    CourseServices sut;

    private CourseRepository mockCourseRepo;

    @Before
    public void beforeTests() {
        mockCourseRepo = mock(CourseRepository.class);
        sut = new CourseServices(mockCourseRepo);
    }

    @After
    public void afterTests() {
        sut = null;
    }

    @Test
    public void isCourseValid_returnsTrue_whenGivenValidInput() {
        // Arrange
        Course inputCourse = new Course("math 102","Discrete Mathematics",
                "describe here","prof",12);

        // Act
        boolean result = sut.isCourseValid(inputCourse);

        // Assert
        Assert.assertTrue(result);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGiven0Capacity() {
        // Arrange
        Course inputCourse = new Course("math 102","Discrete Mathematics",
                "describe here","prof",0);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenLongDescription() {
        // Arrange
        String description = "in hac habitasse platea dictumst vestibulum rhoncus est pellentesque elit ullamcorper " +
                "dignissim cras tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius duis at " +
                "consectetur lorem donec massa sapien faucibus et molestie ac feugiat sed lectus vestibulum mattis " +
                "ullamcorper velit sed ullamcorper morbi tincidunt ornare massa eget egestas purus viverra accumsan " +
                "in nisl nisi scelerisque eu ultrices vitae auctor eu augue ut lectus arcu bibendum at varius vel " +
                "pharetra vel turpis nunc eget lorem dolor sed viverra ipsum nunc aliquet bibendum enim facilisis " +
                "gravida neque convallis a cras semper auctor neque vitae tempus quam pellentesque nec nam aliquam " +
                "sem et tortor consequat id porta nibh venenatis cras sed felis eget velit aliquet sagittis id " +
                "consectetur purus ut faucibus pulvinar elementum integer enim neque volutpat ac tincidunt vitae " +
                "semper quis lectus nulla at volutpat diam ut venenatis tellus in metus vulputate eu scelerisque " +
                "felis imperdiet proin fermentum leo vel orci porta non pulvinar neque laoreet suspendisse interdum " +
                "consectetur libero id faucibus nisl tincidunt eget nullam non nisi est sit amet facilisis magna " +
                "etiam tempor orci eu lobortis elementum nibh tellus molestie nunc non blandit massa enim nec dui " +
                "nunc mattis enim ut tellus elementum sagittis vitae et leo duis ut diam quam nulla porttitor massa " +
                "id neque aliquam vestibulum morbi blandit cursus risus at ultrices mi tempus imperdiet nulla " +
                "malesuada pellentesque elit eget gravida cum sociis natoque penatibus et magnis dis parturient " +
                "montes nascetur ridiculus mus mauris vitae ultricies leo integer malesuada nunc vel risus commodo " +
                "viverra maecenas accumsan lacus vel facilisis volutpat est velit egestas dui id ornare arcu odio ut " +
                "sem nulla pharetra diam sit amet nisl suscipit adipiscing bibendum est ultricies integer quis auctor" +
                " elit sed vulputate mi sit amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar sapien " +
                "et ligula ullamcorper malesuada proin libero nunc consequat interdum varius sit amet mattis " +
                "vulputate enim nulla aliquet porttitor lacus luctus accumsan tortor posuere ac ut consequat semper " +
                "viverra nam libero justo laoreet sit amet cursus sit amet dictum sit amet justo donec enim diam " +
                "vulputate ut pharetra sit amet aliquam id diam maecenas ultricies mi eget mauris pharetra et ultrices " +
                "neque ornare aenean euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis nisl " +
                "rhoncus mattis rhoncus urna neque viverra justo nec ultrices dui sapien eget mi proin sed libero";
        Course inputCourse = new Course("math 102","Discrete Mathematics",
                description,"prof",12);

        // Act & Assert
        boolean result = sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenNullCourseNumber() {
        // Arrange
        Course inputCourse = new Course(null,"Discrete Mathematics",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenNullCourseName() {
        // Arrange
        Course inputCourse = new Course("math 102",null,
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenEmptyCourseNumber() {
        // Arrange
        Course inputCourse = new Course("","Discrete Mathematics",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenWhiteSpaceCourseNumber() {
        // Arrange
        Course inputCourse = new Course("     ","Discrete Mathematics",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenShortCourseNumber() {
        // Arrange
        Course inputCourse = new Course("short","Discrete Mathematics",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenEmptyCourseName() {
        // Arrange
        Course inputCourse = new Course("math 102","",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenWhiteSpaceCourseName() {
        // Arrange
        Course inputCourse = new Course("math 102","    ",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test(expected = InvalidInformationException.class)
    public void isCourseValid_throwsInvalidInformationException_whenGivenShortCourseName() {
        // Arrange
        Course inputCourse = new Course("math 102","short",
                "description","prof",12);

        // Act & Assert
        sut.isCourseValid(inputCourse);
    }

    @Test
    public void createCourse_returnsSuccessfully_whenGivenValidCourse() {
        // Arrange
        Course inputCourse = new Course("math 102","Discrete Mathematics",
                "describe here","prof",12);
        when(mockCourseRepo.save(inputCourse)).thenReturn(inputCourse);

        // Act
        Course returnCourse = sut.createCourse(inputCourse);

        // Arrange
        verify(mockCourseRepo,times(1)).save(inputCourse);
        Assert.assertEquals(inputCourse.getNumber(),returnCourse.getNumber());
        Assert.assertEquals(inputCourse.getName(),returnCourse.getName());
        Assert.assertEquals(inputCourse.getDescription(),returnCourse.getDescription());
        Assert.assertEquals(inputCourse.getProfessor(),returnCourse.getProfessor());
        Assert.assertEquals(inputCourse.getCapacity(),returnCourse.getCapacity());
    }

    @Test
    public void getRegisteredCourses_returnsNull_ifNoCoursesFoundForStudent() {
        // Arrange
        Student student = new Student();
        when(mockCourseRepo.findByStudent(student)).thenReturn(null);

        // Act
        List<Course> result = sut.getRegisteredCourses(student);

        // Assert
        verify(mockCourseRepo,times(1)).findByStudent(student);
        Assert.assertEquals(result,null);
    }

    @Test
    public void getRegisteredCourses_catchesException_whenExceptionOccurs() {
        // Arrange
        Student student = new Student();
        DataSourceException dse = new DataSourceException("", new Exception());
        when(mockCourseRepo.findByStudent(any())).thenThrow(dse);

        // Act
        sut.getRegisteredCourses(student);

        // Assert
        verify(mockCourseRepo,times(1)).findByStudent(student);
    }

    @Test
    public void getTaughtCourses_returnsNull_ifNoCourseFoundForFaculty() {
        // Arrange
        Faculty faculty = new Faculty();
        when(mockCourseRepo.findByFaculty(faculty)).thenReturn(null);

        // Act
        List<Course> result = sut.getTaughtCourses(faculty);

        // Assert
        verify(mockCourseRepo,times(1)).findByFaculty(faculty);
        Assert.assertEquals(result,null);
    }

    @Test (expected = DataSourceException.class)
    public void getTaughtCourses_throwsException_whenExceptionOccurs() {
        // Arrange
        Faculty faculty = new Faculty();
        DataSourceException dse = new DataSourceException("", new Exception());
        when(mockCourseRepo.findByFaculty(any())).thenThrow(dse);

        // Act
        sut.getTaughtCourses(faculty);

        // Assert
        verify(mockCourseRepo,times(1)).findByFaculty(faculty);
    }

    @Test
    public void getCourseList_returnsNull_ifNoCourseFound() {
        // Arrange
        when(mockCourseRepo.findAll()).thenReturn(null);

        // Act
        List<Course> result = sut.getCourseList();

        // Assert
        verify(mockCourseRepo,times(1)).findAll();
        Assert.assertEquals(result,null);
    }

    @Test (expected = DataSourceException.class)
    public void getCourseList_throwsException_whenExceptionOccurs() {
        // Arrange
        DataSourceException dse = new DataSourceException("", new Exception());
        when(mockCourseRepo.findAll()).thenThrow(dse);

        // Act
        sut.getCourseList();

        // Assert
        verify(mockCourseRepo,times(1)).findAll();
    }

    @Test
    public void registerForCourse_returnsSuccessfully_givenValidInput() {
        // Arrange
        String number = "math 100";
        String email = "student@school.com";

        when(mockCourseRepo.addStudent(number, email)).thenReturn(true);

        // Act
        boolean result = sut.registerForCourse(number, email);

        // Assert
        verify(mockCourseRepo,times(1)).addStudent(number, email);
        Assert.assertTrue(result);
    }

    @Test (expected = Exception.class)
    public void registerForCourse_throwsException_whenExceptionOccurs() {
        // Arrange
        String number = "math 100";
        String email = "student@school.com";
        DataSourceException dse = new DataSourceException("An unexpected exception occurred while trying to register student to course", new Exception());

        when(mockCourseRepo.addStudent(number, email)).thenThrow(dse);

        // Act
        sut.registerForCourse(number, email);

        // Assert
        verify(mockCourseRepo,times(1)).addStudent(number, email);
    }



    @Test(expected = DataSourceException.class)
    public void registerForCourse_throwsDataSourceException_whenStudentAlreadyInClass() {
        // Arrange
        String number = "cs 100";
        Student student = new Student();
        student.setFirstName("Chaddington");
        student.setLastName("Wellsworth");
        student.setEmail("chad@wellsworth.com");
        student.setPassword("pretentious");
        when(mockCourseRepo.addStudent(any(),any())).thenThrow(RuntimeException.class);

        // Act
        try {
            sut.registerForCourse(number, student);
        } finally {
            // Assert
            verify(mockCourseRepo, times(1)).addStudent(number, student.getEmail());
        }
    }

    @Test
    public void removeCourse_returnsFalse_ifCourseIsNotInDatabase() {
        // Arrange
        String number = "mus 400";
        when(mockCourseRepo.deleteByNumber(any())).thenReturn(false);

        // Act
        boolean result = sut.removeCourse(number);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void removeCourse_returnsTrue_ifCourseIsInDatabase(){
        // Arrange
        Course course = new Course();
        course.setId("fakeId");
        course.setNumber("test 101");
        course.setName("Testing");
        course.setDescription("valid description");
        course.setCapacity(10);
        course.setStudents(new String[]{"a", "b", "c"});

        sut.createCourse(course);

        when(mockCourseRepo.deleteByNumber(course.getNumber())).thenReturn(true);

        // Act
        boolean result = sut.removeCourse(course.getNumber());

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void removeFromCourse_returnsTrue_whenGivenValidInput() {
        // Arrange
        String number = "math 100";
        String field = "capacity";
        String newValue = "15";

        when(mockCourseRepo.update(number, field, newValue)).thenReturn(true);

        // Act
        boolean result = sut.updateCourse(number, field, newValue);

        // Assert
        verify(mockCourseRepo,times(1)).update(number, field, newValue);
        Assert.assertTrue(result);
    }

}
