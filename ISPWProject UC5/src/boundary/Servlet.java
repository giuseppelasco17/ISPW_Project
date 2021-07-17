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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Funzione che acquisisce dati in input dalla pagina web, ne verifica la validità e li prepara, mediante una
        * lista per consegnarli al controller. Ed infine li stampa in una tabella organizzata in un documento html*/
        ArrayList<String> content = new ArrayList<String>();
        List<Booking> bookings;
        if (validateInput(request.getParameter("begin"), request.getParameter("end"))){
            /*Se i dati inseriti sono validi prepara la lista. Nel caso in cui la tipologia è 'conferenza', essendo
            * disabilitati, non preleva i dati in input relativi a corso e sessione
            */
            content.add(request.getParameter("list"));
            if(request.getParameter("list").equals("esame")) {
                content.add(request.getParameter("course").toLowerCase());
                content.add(request.getParameter("session").toLowerCase());
            }
            else {
                content.add("");
                content.add("");
            }
            content.add(request.getParameter("date"));
            content.add(request.getParameter("begin"));
            content.add(request.getParameter("end"));
            bookings = controller.handleFilter(content);
            /*Formattazione, realizzazione e riempimento della tabella risultante in una pagina html*/
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.print("<body bgcolor=\"#343434\"><style>\n" +
                    "table { width: 100%; background-color: #343434; color: #FFF; border-color: #ECDBFF; border-collapse: collapse; }\n" +
                    "th, td { width: 12.5%; }\n" +
                    "</style>\n" +
                    "<table border=\"2\" cellspacing=\"0\" cellpadding=\"10\" align=\"center\">\n" +
                    "<thead>\n" +
                    "<tr><th>Materia</th><th>Data</th><th>Sessione</th><th>Aula</th>\n" +
                    "<th>Ora Inizio</th><th>Ora Fine</th><th>Data Prenotazione</th></tr></thead><tbody>");
            for(int i = 0; i < bookings.size(); i++)
            {
                String[] result = new String[8];
                result[0] = bookings.get(i).getCourse();
                result[1] = bookings.get(i).getDate();
                result[2] = bookings.get(i).getSession();
                result[3] = bookings.get(i).getClassroom();
                result[4] = bookings.get(i).getBegin();
                result[5] = bookings.get(i).getEnd();
                result[6] = bookings.get(i).getBookingDate();
                out.print("<tr>");
                for(int j = 0; j<7; j++) {
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

    private boolean validateInput(String begin, String end){
        /*Funzione che valida i dati inseriti in input. In caso di dati non validi, setta le opportune label di errore*/
        boolean flag = true;
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
        return flag;
    }
}
