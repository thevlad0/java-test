/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SUSI {
    public enum UserType {
        STUDENT,
        TEACHER
    }

    private class User {
        UserType type = null;
        private LocalDate dateAdded;
        protected String idString;
        protected String name;
    
        public User(String idString, String name)
        {
            dateAdded = LocalDate.now();
            this.idString = idString;
            this.name = name;
        }
    
        public void printUserInfo() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    
            System.out.println(String.format("Date added: %s", dtf.format(dateAdded)));
            System.out.println(String.format("Name: %s", name));
        }
    }

    private class ElectiveCourse {
        public String name;
        public HashMap<String, User> teachers;
        public HashMap<String, User> students;
    
        public ElectiveCourse(String name) {
            this.name = name;
        }

        public void addStudent(String idString) {
            students.put(idString, users.get(idString));
        }

        public void removeStudent(String idString) {
            students.remove(idString);
        }

        public boolean hasStudent(String idString) {
            return students.containsKey(idString);
        }
    }

    private class Student extends User {
        public String facultyNum;
        public int semester = 1;
        public List<ElectiveCourse> courses;
    
        public Student(String idString, String name, String facultyNum) {
            super(idString, name);
            this.facultyNum = facultyNum;
        }

        public void addElectiveCourse(ElectiveCourse course) {
            courses.add(course);
        }

        public void removeElectiveCourse(ElectiveCourse course) {
            for (ElectiveCourse electiveCourse : courses) {
                if(electiveCourse == course) {
                    courses.remove(electiveCourse);
                }
            }
        }

        public void printUserInfo() {
            super.printUserInfo();
            System.out.println(String.format("Faculty number: %s", facultyNum));
            System.out.println(String.format("Semester: %d", semester));
        }
    }

    private class Teacher extends User {
        public int experience;
        public String department;

        public Teacher(String idString, String name) {
            super(idString, name);
        }

        public void printUserInfo() {
            super.printUserInfo();
            System.out.println(String.format("Department: %s", department));
            System.out.println(String.format("Experience: %s", experience));
        }
    }

    private static HashMap<String, User> users;
    private static HashMap<String, ElectiveCourse> courses;
    private static Integer userNum = 0;

    public static void printUserInfo(String idString) {
        users.get(idString).printUserInfo();
    }

    public static void addUser(String idString, String name, UserType userType)
    {
        switch (userType) {
            case STUDENT: {
                User u = new SUSI().new Student(idString, name, genFacultyNum(name));
                users.put(idString, u);
            } break;
            
            case TEACHER: {

                User u = new SUSI().new Teacher(idString, name);
                users.put(idString, u);
            } break;
        
            default:
                break;
        }
    }

    public static void removeUser(String idString)
    {
        users.remove(idString);

        // If user was in any course
        courses.forEach( (id, course) -> {
            if(course.hasStudent(idString)) {
                course.removeStudent(idString);
            }
        });
    }

    public void addCourse(String name) {
        courses.put(name, new ElectiveCourse(name));
    }

    public void addStudentToElective(String idString, String name) {
        courses.get(name).addStudent(idString);
    }

    public void removeStudentToElective(String idString, String name) {
        courses.get(name).removeStudent(idString);
    }

    private static String genFacultyNum(String name)
    {
        StringBuilder fNum = new StringBuilder();
        fNum.append(name.charAt(0));
        fNum.append(userNum);
        ++userNum;

        return fNum.toString();
    }
}
