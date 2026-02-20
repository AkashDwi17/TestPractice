package Test.example.Test.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {

    public String getFinalSqlQuery(String regNo) {

        // Get last two digits
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int number = Integer.parseInt(lastTwoDigits);

        if (number % 2 == 0) {
            // EVEN → Question 2
            return getQuestion2Solution();
        } else {
            // ODD → Question 1
            return getQuestion1Solution();
        }
    }

    private String getQuestion1Solution() {

        // Question 1 SQL (keep for safety)
        return "SELECT d.DEPARTMENT_NAME, emp_salary.total_salary AS SALARY, " +
                "CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
                "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE " +
                "FROM (SELECT e.DEPARTMENT, e.EMP_ID, SUM(p.AMOUNT) AS total_salary, " +
                "ROW_NUMBER() OVER (PARTITION BY e.DEPARTMENT ORDER BY SUM(p.AMOUNT) DESC) AS rn " +
                "FROM EMPLOYEE e JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
                "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                "GROUP BY e.DEPARTMENT, e.EMP_ID) emp_salary " +
                "JOIN EMPLOYEE e ON emp_salary.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE emp_salary.rn = 1;";
    }

    private String getQuestion2Solution() {

        // ✅ EVEN REG NO → THIS WILL BE USED
        return "SELECT d.DEPARTMENT_NAME, " +
                "AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE, " +
                "SUBSTRING_INDEX(GROUP_CONCAT(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) " +
                "ORDER BY e.EMP_ID SEPARATOR ', '), ', ', 10) AS EMPLOYEE_LIST " +
                "FROM EMPLOYEE e " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "JOIN (SELECT EMP_ID, SUM(AMOUNT) AS total_salary " +
                "FROM PAYMENTS GROUP BY EMP_ID HAVING SUM(AMOUNT) > 70000) high_earners " +
                "ON e.EMP_ID = high_earners.EMP_ID " +
                "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
                "ORDER BY d.DEPARTMENT_ID DESC;";
    }
}