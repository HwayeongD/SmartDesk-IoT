import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartdesk.R;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private List<String> dayList;

    public CalendarAdapter(List<String> dayList) {

        this.dayList = dayList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String day = dayList.get(position);
        holder.dayTextView.setText(day);
    }

    @Override
    public int getItemCount() {

        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView; // Update to match your layout

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.day_text_view); // Update with your view ID
        }
    }
}
