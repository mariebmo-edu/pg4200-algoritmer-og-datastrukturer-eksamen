import java.util.*;

import java.util.stream.Collectors;

public class Ex04 {

    public Double A(Student[] students, String courseId){

        return Arrays.stream(students)
                .flatMap(s -> s.getExamPoints().entrySet().stream())
                .filter(c -> courseId.equals(c.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.averagingDouble(x->x));
    }

    public List<Ex04.Student> B(Course[] courses){

        List<Student> studentsInAcceptedCourses = Arrays.stream(courses)
                .filter(e -> e.getEvaluation() != null)
                .filter(e -> Arrays.asList(e.topics.split(" ")).contains("programming")) //you can have multiple topics in a course
                .flatMap(e -> e.getPoints().keySet().stream())
                .distinct()
                .collect(Collectors.toList());


        StudentComparator studentComparator = new StudentComparator();

        Collections.sort(studentsInAcceptedCourses, studentComparator);
        System.out.println(studentsInAcceptedCourses.size());

        for(Student s : studentsInAcceptedCourses){
            System.out.println("id:" + s.student_id + " name:" + s.name + "    algdat-points:" + s.getExamPoints().get("pg4200") + "    avjava-points:" + s.getExamPoints().get("pgr203")+ "    softwaredesign-points:" + s.getExamPoints().get("pg3302"));
        }

        /*
        (Attempt to do it fully by streams - but I didn't manage to get the comparison by subject number to work as intended)
        return Arrays.stream(courses)
                .filter(e -> e.getEvaluation() != null)
                .filter(e -> Arrays.asList(e.topics.split(" ")).contains("programming")) //you can have multiple topics in a course
                .flatMap(e -> e.getPoints().entrySet().stream())
                .sorted(Comparator.comparing(e -> e.getValue()*-1))
                .map(a -> a.getKey().name)
                .distinct()
                .collect(Collectors.toList());
         */

        return studentsInAcceptedCourses;

    }

    public static class Student{

        public int student_id;
        public String name;

        //a map that takes a course id as a key and stores the grade as a value, for each course this student has taken.
        Map<String, Double> examPoints;

        public int getStudent_id() {
            return student_id;
        }

        public void setStudent_id(int student_id) {
            this.student_id = student_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Double> getExamPoints() {
            return examPoints;
        }

        public void setExamPoints(Map<String, Double> examPoints) {
            this.examPoints = examPoints;
        }
    }

    public static class Course{
        String course_code;
        String topics;
        String evaluation;
        Map<Student, Integer> points;

        public String getCourse_code() {
            return course_code;
        }

        public void setCourse_code(String course_code) {
            this.course_code = course_code;
        }

        public String getTopics() {
            return topics;
        }

        public void setTopics(String topics) {
            this.topics = topics;
        }

        public String getEvaluation() {
            return evaluation;
        }

        public void setEvaluation(String evaluation) {
            this.evaluation = evaluation;
        }

        public Map<Student, Integer> getPoints() {
            return points;
        }

        public void setPoints(Map<Student, Integer> points) {
            this.points = points;
        }
    }

    public class StudentComparator implements Comparator<Student>{



        @Override
        public int compare(Student s1, Student s2) {

            double examDiff =  s2.getExamPoints().get("pg4200") - s1.getExamPoints().get("pg4200");

            if(examDiff == 0){
                return s1.student_id - s2.student_id;
            }

            return (int)examDiff;
        }
    }
}
