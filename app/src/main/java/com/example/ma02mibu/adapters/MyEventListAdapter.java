package com.example.ma02mibu.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.employees.EmployeeDetailsFragment;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;
import com.example.ma02mibu.fragments.employees.EmployeeWorkCalendarFragment;
import com.example.ma02mibu.fragments.events.EventAgendaFragment;
import com.example.ma02mibu.fragments.events.EventGuestListFragment;
import com.example.ma02mibu.model.AgendaItem;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Guest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyEventListAdapter extends ArrayAdapter<Event>{
    private final ArrayList<Event> aEvents;
    private final String ownerRefId;

    private Context contextAD;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FragmentActivity currFragActivity;
    public MyEventListAdapter(Context context, ArrayList<Event> events, String ownerRefId, FragmentActivity fragmentActivity){
        super(context, R.layout.fragment_event_card, events);
        contextAD = context;
        aEvents= events;
        this.ownerRefId = ownerRefId;
        currFragActivity = fragmentActivity;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }
    @Override
    public int getCount() {
        return aEvents.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return aEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }








    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_event_card,
                    parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.event_name);
         Button agendaButton = convertView.findViewById(R.id.button_event_agenda);
         handleAgendaButtonClick(agendaButton, event);
         Button guestListButton = convertView.findViewById(R.id.button_event_guest_list);
         handleGuestListButtonClick(guestListButton, event);
         Button guestListPdfButton = convertView.findViewById(R.id.button_event_guest_pdf);
         handleGuestListPdfButtonClick(guestListPdfButton, event);
         Button agendaPdfButton = convertView.findViewById(R.id.button_event_agenda_pdf);
         handleAgendaPdfButtonClick(agendaPdfButton, event);

         if(event != null){
             eventName.setText(event.getName());
        }

        return convertView;
    }


    private void handleAgendaButtonClick(Button detailsButton, Event event){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EventAgendaFragment.newInstance(event, ownerRefId), currFragActivity,
                        true, R.id.scroll_events_list, "EventAgendaPage");
            }
        });
    }

    private void handleGuestListButtonClick(Button detailsButton, Event event){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EventGuestListFragment.newInstance(event, ownerRefId), currFragActivity,
                        true, R.id.scroll_events_list, "EventGLPage");
            }
        });
    }

    private void handleGuestListPdfButtonClick(Button detailsButton, Event event){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateGuestListPdf(contextAD, event);
            }
        });
    }

    private void handleAgendaPdfButtonClick(Button detailsButton, Event event){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAgendaPdf(contextAD, event);
            }
        });
    }



    public void generateGuestListPdf(Context context, Event event) {
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.RED);
        paint.setTextSize(36);

        canvas.drawText("Guest list", 100, 100, paint);

        paint.setTextSize(30);
        canvas.drawText("Event: " + event.getName(), 100, 150, paint);

        paint.setTextSize(24);
        int startX = 5;
        int startY = 250;
        int rowHeight = 50;
        int columnWidth = 250;
        canvas.drawText("Name", startX, startY, paint);
        canvas.drawText("Age", startX + columnWidth, startY, paint);
        canvas.drawText("Invited", startX + 2 * columnWidth, startY, paint);
        canvas.drawText("Accepted", startX + 3 * columnWidth, startY, paint);
        canvas.drawText("Special", (float) (startX + 3.7 * columnWidth), startY, paint);
        canvas.drawLine(startX, startY + 10, startX + 4 * columnWidth, startY + 10, paint);

        if(event.getGuests() == null){
            return;
        }

        int currentY = startY + rowHeight;
        for (Guest guest : event.getGuests()) {
            canvas.drawText(guest.getName(), startX, currentY, paint);
            canvas.drawText(String.valueOf(guest.getAge()), startX + columnWidth, currentY, paint);
            canvas.drawText(String.valueOf(guest.getInvited()), startX + 2 * columnWidth, currentY, paint);
            canvas.drawText(String.valueOf(guest.getHasAccepted()), startX + 3 * columnWidth, currentY, paint);
            canvas.drawText(guest.getSpecial(), (float) (startX + 3.7 * columnWidth), currentY, paint);
            currentY += rowHeight;
        }

        document.finishPage(page);

        String fileName = event.getName() + "_guest_list.pdf";
        download(context, document, fileName);

        document.close();
    }


    public void generateAgendaPdf(Context context, Event event) {
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.RED);
        paint.setTextSize(36);

        canvas.drawText("Agenda", 100, 100, paint);

        paint.setTextSize(30);
        canvas.drawText("Event: " + event.getName(), 100, 150, paint);

        paint.setTextSize(24);
        int startX = 100;
        int startY = 250;
        int rowHeight = 50;
        int columnWidth = 250;
        canvas.drawText("Name", startX, startY, paint);
        canvas.drawText("Description", startX + columnWidth, startY, paint);
        canvas.drawText("Timespan", startX + 2 * columnWidth, startY, paint);
        canvas.drawText("Location", startX + 3 * columnWidth, startY, paint);
        canvas.drawLine(startX, startY + 10, startX + 4 * columnWidth, startY + 10, paint);

        if(event.getAgenda() == null){
            return;
        }


        int currentY = startY + rowHeight;
        for (AgendaItem ai : event.getAgenda()) {
            canvas.drawText(ai.getName(), startX, currentY, paint);
            canvas.drawText(ai.getDescription(), startX + columnWidth, currentY, paint);
            canvas.drawText(ai.getTimespan(), startX + 2 * columnWidth, currentY, paint);
            canvas.drawText(ai.getLocation(), startX + 3 * columnWidth, currentY, paint);
            currentY += rowHeight;
        }

        document.finishPage(page);

        String fileName = event.getName() + "_agenda.pdf";
        download(context, document, fileName);

        document.close();
    }



    private void download(Context context, PdfDocument document, String fileName) {
        String directoryPath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
        File file = new File(directoryPath, fileName);
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Report downloaded", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Report download failed", Toast.LENGTH_LONG).show();
        }
    }



}

