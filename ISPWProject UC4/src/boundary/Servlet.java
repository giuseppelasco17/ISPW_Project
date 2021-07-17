package boundary;

import control.Controller;
import entity.Booking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "boundary.Servlet", urlPatterns = "/boundary.Servlet")
public class Servlet extends HttpServlet {
    /**
     * Questa classe rappresenta un altro entry-point, ovvero quello web. Servlet eredita da HttpServlet: fornisce una
     * classe astratta che deve essere implementata da una sottoclasse per creare un HTTP servlet utilizzabile per un
     * sito web
     */
    private Controller controller = new Controller();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Funzione che acquisisce dati in input dalla pagina web, ne verifica la validità e li prepara, mediante una
         * lista per consegnarli al controller. Ed infine li stampa in una tabella organizzata in un documento html*/
        ArrayList<String> content = new ArrayList<String>();
        List<Booking> bookings;
        List<String> teacherList;
        if ((teacherList = validateInput(request.getParameter("begin"), request.getParameter("end"),
                request.getParameter("teacherName").toLowerCase())) != null){
            /*Se i dati inseriti sono validi prepara la lista. Nel caso in cui la tipologia è 'conferenza', essendo
             * disabilitati, non preleva i dati in input relativi a corso
             */
            content.add(request.getParameter("list"));
            content.add(teacherList.get(0));
            content.add(teacherList.get(1));
            if(request.getParameter("list").equals("esame"))
                content.add(request.getParameter("course").toLowerCase());
            else
                content.add("");
            content.add(request.getParameter("date"));
            content.add(request.getParameter("begin"));
            content.add(request.getParameter("end"));
            bookings = controller.handleFilter(content);
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.print("<body bgcolor=\"#343434\"><style>\n" +
                    "table { width: 100%; background-color: #343434; color: #FFF; border-color: #ECDBFF; border-collapse: collapse; }\n" +
                    "th, td { width: 12.5%; }\n" +
                    "</style>\n" +
                    "<table border=\"2\" cellspacing=\"0\" cellpadding=\"10\" align=\"center\">\n" +
                    "<thead>\n" +
                    "<tr><th>Nome</th><th>Cognome</th><th>Data</th><th>Aula</th><th>Materia</th><th>Sessione</th>\n" +
                    "<th>Ora Inizio</th><th>Ora Fine</th></tr></thead><tbody>");
            for(int i = 0; i < bookings.size(); i++)
            {
                String[] result = new String[8];
                result[0] = bookings.get(i).getBookerName();
                result[1] = bookings.get(i).getBookerSurname();
                result[2] = bookings.get(i).getDate();
                result[3] = bookings.get(i).getClassroom();
                result[4] = bookings.get(i).getCourse();
                result[5] = bookings.get(i).getSession();
                result[6] = bookings.get(i).getBegin();
                result[7] = bookings.get(i).getEnd();
                out.print("<tr>");
                for(int j = 0; j<8; j++) {
                    out.print("<td>" + result[j] + "</td>");
                }
                out.print("</tr>");
            }
            out.print("</tbody></body></table>");
        }
        else
        {
            response.sendRedirect("indexError.jsp");
        }
    }

    private List<String> validateInput(String begin, String end, String teacher){
        /*Funzione che valida i dati inseriti in input. In caso di dati non validi, setta le opportune label di errore*/
        boolean flag = true;
        List<String> teacherList = new ArrayList<>();
        if(!teacher.isEmpty()) {
            teacherList = controller.splitTeacher(teacher);
            if(!controller.validateTeacher(teacherList)) {
                flag = false;
            }
        }else {
            teacherList.add("");
            teacherList.add("");
        }
        if(!begin.isEmpty()) {
            if (!controller.timeParse(begin)) {
                flag = false;
            }
        }
        if(!end.isEmpty()) {
            if (!controller.timeParse(end)) {
                flag = false;
            }
        }
        if(flag && !begin.isEmpty() && !end.isEmpty()) {
            if(end.compareTo(begin) < 0) {
                flag = false;
            }
        }
        if(!flag){
            return null;
        }
        return teacherList;
    }
}
