package savion.tns.com.savion;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by DELL on 22-Aug-18.
 */

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private List<tickets> bookings;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number, name, mobile,place,date,time;
        public ImageView image;
        public TextView textbottom;

        public MyViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.vehicleno);
            name = (TextView) view.findViewById(R.id.name);
            mobile = (TextView) view.findViewById(R.id.mobile);
            place = (TextView) view.findViewById(R.id.place);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
            textbottom = (TextView) view.findViewById(R.id.txtBottom);
            image = (ImageView) view.findViewById(R.id.profile_image);
        }
    }


    public BookingAdapter(List<tickets> moviesList) {
        this.bookings = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tickettest, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        tickets tickets = bookings.get(position);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();

// declare the builder object once.
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable drawable = builder.build("", color1);

        holder.number.setText(tickets.getVehicleno());
        holder.name.setText(tickets.getName());
        holder.mobile.setText(tickets.getMobile());
        holder.place.setText(tickets.getPlace());

        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date c = Calendar.getInstance().getTime();
        try {
            Date date1 = simpleDateFormat.parse(simpleDateFormat.format(c));
            Date date2 = simpleDateFormat.parse(tickets.getDate());


            long different = date2.getTime() - date1.getTime();


            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays<0){
                //holder.textbottom.setTextColor(R.color.red_background);
                holder.textbottom.setText("Expired");
                //holder.date.setText(String.valueOf(elapsedDays));
            }else {
                //holder.date.setText(String.valueOf(elapsedDays));
                holder.textbottom.setText(elapsedDays+" Days Left");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String cdate = sdf.format(c.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;
        Date current=Calendar.getInstance().getTime();
        try {
            d1 = format.parse(tickets.getDate());
            d2 = format.parse(cdate);
            long diff = d1.getTime() - d2.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            if (diffDays<0){
                //holder.textbottom.setTextColor(R.color.red_background);
                holder.textbottom.setText("Expired");
                //holder.date.setText(String.valueOf(elapsedDays));
            }else {
                //holder.date.setText(String.valueOf(elapsedDays));
                holder.textbottom.setText(diffDays+" Days Left");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.date.setText(tickets.getDate());
        holder.time.setText(tickets.getTime());
        holder.image.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}