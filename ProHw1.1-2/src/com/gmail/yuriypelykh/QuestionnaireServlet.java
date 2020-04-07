package com.gmail.yuriypelykh;

import javax.servlet.http.*;
import java.io.IOException;

public class QuestionnaireServlet extends HttpServlet{

    static final int q1yes = 0;
    static final int q1no = 1;
    static final int q2yes = 2;
    static final int q2no = 3;

    final int[] results = new int[4];

    static final String TEMPLATE = "<html>" +
            "<head><title>Questionnaire</title></head>" +
            "<body><h1>%s</h1></body></html>";

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
      //  int age = Integer.parseInt(req.getParameter("age"));
        String q1 = req.getParameter("question1");
        String q2 = req.getParameter("question2");
        final int q1Answ = "yes".equals(q1) ? q1yes : q1no;
        final int q2Answ = "yes".equals(q2) ? q2yes : q2no;
        results[q1Answ]++;
        results[q2Answ]++;
        String msg = "<p>Statistics:<br>" +
                "Do you like Java?<br>" +
                "Yes - " + results[q1yes] + "<br>"+
                "No - " + results[q1no] + "<br>"+
                "Do you like .NET?<br>"+
                "Yes - " + results[q2yes] + "<br>"+
                "No - " + results[q2no] + "<br>"+"</p>";

        resp.getWriter().println(String.format(TEMPLATE, msg));
    }
}
