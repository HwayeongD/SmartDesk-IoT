package com.example.smartdesk.ui.seat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.smartdesk.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private final String TAG = "GridViewAdapter";

    private Fragment seatFragment;

    ArrayList<SeatItem> seats = new ArrayList<SeatItem>();

    public GridViewAdapter(Fragment seatFragment) {
        this.seatFragment = seatFragment;
    }

    @Override
    public int getCount() {
        return seats.size();
    }

    public void addItem(SeatItem seat) {
        seats.add(seat);
        Log.d("GridViewAdapter", "Add A Seat: " + seat.getSeatId());
    }

    public void clearItems() {
        seats.clear();
    }

    @Override
    public Object getItem(int i) {
        return seats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        SeatItem seatItem = seats.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.seat_each, viewGroup, false);
        }

        ConstraintLayout layoutSeat = (ConstraintLayout) convertView.findViewById(R.id.layout_seat);
        ImageView isOnline = (ImageView) convertView.findViewById(R.id.emp_status);
        TextView teamName = (TextView) convertView.findViewById(R.id.team_name);
        TextView empName = (TextView) convertView.findViewById(R.id.emp_name);
        TextView seatId = (TextView) convertView.findViewById(R.id.seat_id);

        if (seatItem.getNickname() == null || seatItem.getNickname().equals("")) { // 예약 가능 좌석
            layoutSeat.setBackgroundResource(R.drawable.seat_yellow);
            seatId.setText(seatItem.getSeatId().toString());
            seatId.setVisibility(View.VISIBLE);
        } else { // 예약된 좌석
            layoutSeat.setBackgroundResource(R.drawable.seat_gray);
            if (seatItem.getStatus() == 1)
                isOnline.setBackgroundResource(R.drawable.circle_online);
            else
                isOnline.setBackgroundResource(R.drawable.circle_offline);
            teamName.setText(seatItem.getTeamName());
            empName.setText(seatItem.getNickname());

            isOnline.setVisibility(View.VISIBLE);
            teamName.setVisibility(View.VISIBLE);
            empName.setVisibility(View.VISIBLE);
        }
//        } else {
//            View view = new View(context);
//            view = (View) convertView;
//        }

        // 각 좌석 아이템 선택 event
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, seatItem.getSeatId() + " is clicked");
                ((SeatFragment) seatFragment).eachSeatEvent(seatItem);
            }
        });

        return convertView;
    }
}
