package com.example.smartdesk.ui.seat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.Model.Seat;
import com.example.smartdesk.data.RetrofitAPI;
import com.example.smartdesk.data.RetrofitClient;
import com.example.smartdesk.databinding.FragmentSeatBinding;
import com.example.smartdesk.ui.dialog.CheckDialog;
import com.example.smartdesk.ui.dialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeatFragment extends Fragment {
    private final String TAG = "SeatFragment";
    private FragmentSeatBinding binding;

    Retrofit retrofit = RetrofitClient.getClient();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    private GridView gridView = null;
    private GridViewAdapter adapter = null;
//    private RecyclerView recyclerView;
//    private RecyclerViewAdapter adapter;
//    private GridLayoutManager layoutManager;

    private ArrayList<SeatItem> seatList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SeatViewModel seatViewModel =
                new ViewModelProvider(this).get(SeatViewModel.class);

        //binding = FragmentSeatBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        View root = inflater.inflate(R.layout.fragment_seat, container, false);

        gridView = (GridView) root.findViewById(R.id.gridview_seat);
        adapter = new GridViewAdapter(this);

//        Spinner floorSpinner = root.findViewById(R.id.floorSpinner);
//        floorSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                reqFloorSeat(i);
//            }
//        });

        reqFloorSeat(0); // 초기값은 2층(0번 인덱스)

//        recyclerView = root.findViewById(R.id.grid_recyclerview);
//        adapter = new RecyclerViewAdapter(getContext(), seatList);
//
//        layoutManager = new GridLayoutManager(getContext(), 2);
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);

        return root;
    }

    // floor: [2, 3, 4]
    private void reqFloorSeat(int floor) {
        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        retrofitAPI.reqFloorSeat(floor + 2).enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                List<Seat> data = response.body();

                int sz = data.size();
                for(int i=0;i<sz;i++){
                    Seat dSeat= data.get(i);
                    SeatItem cSeat = new SeatItem();
                    Boolean isOnline = dSeat.getStatus().equals("false") ? false : true;
                    cSeat.setSeatId(dSeat.getSeatId());
                    cSeat.setNickname(dSeat.getNickname());
                    cSeat.setTeamName(dSeat.getTeamName());
                    cSeat.setStatus(isOnline);

                    adapter.addItem(cSeat);
                    Log.d(TAG, cSeat.getSeatId() + ", " + cSeat.getNickname() + ", " + cSeat.getTeamName() + ", " + cSeat.getStatus());
                }

                gridView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {

            }
        });
    }

    public void eachSeatEvent(SeatItem item) {
        Log.d(TAG, "seatId: " + Employee.getInstance().getSeatId() + ", seatItem: " + item.getSeatId());
        // 1-1) 사원이 좌석 예약을 한 경우
        if(Employee.getInstance().getSeatId() != null && !Employee.getInstance().getSeatId().isEmpty()) {
            if(item.getSeatId().equals(Employee.getInstance().getSeatId())) { // 2-1) 본인 좌석을 선택한 경우
                // 취소 할래?
                showCancelDialog();
            } else { // 2-2) 본인 좌석 이외를 선택한 경우
                if(item.getNickname() == null || item.getNickname().isEmpty()) { // 3-1) 공석인 경우
                    // 변경 할래?
                    showChangeDialog();
                } else { // 3-2) 점유된 좌석인 경우
                    // 예약된 좌석이라고 안내!
                    showOccupiedDialog();
                }
            }
        } else { // 1-2) 사원이 좌석 예약을 안 한 경우
            if(item.getNickname() == null || item.getNickname().isEmpty()) { // 2-1) 공석인 경우
                // 예약 할래?
                showReserveDialog();
            } else { // 2-2) 점유된 좌석인 경우
                // 예약된 좌석이라고 안내!
                showOccupiedDialog();
            }
        }
    }

    // 예약 확인 팝업
    private void showReserveDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog(this.getContext(), R.drawable.ic_check_circle_48px, "예약 확인", "예약이 확정되었습니다.");

        confirmDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {

            }
        });

        confirmDialog.show();
    }

    //좌석 변경 체크 팝업
    private void showChangeDialog() {
        CheckDialog changeDialog = new CheckDialog(this.getContext(), R.drawable.ic_error_48px, "예약 변경", "이미 예약된 좌석이 있습니다\n좌석을 변경하시겠습니까?");

        changeDialog.setDialogListener(new CheckDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {
                retrofitAPI.reqChangeSeat(Employee.getInstance()).enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        Employee data = response.body();
                        if(data.getResultCode().equals("S101")) {
                            Employee.getInstance().setSeatId(data.getSeatId());

                            // 예약 변경 확인 팝업
                            ConfirmDialog confirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_check_circle_48px, "예약 확인", "예약이 변경되었습니다.");

                            confirmDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
                                @Override
                                public void okBtnClicked(String btnName) {

                                }
                            });

                            confirmDialog.show();
                        } else if(data.getResultCode().equals("S201")) {
                            Log.d(TAG, "reqChangeSeat(): 이미 예약되었습니다.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {
                        Log.d(TAG, "reqChangeSeat() 통신 실패");
                    }
                });
            }

            @Override
            public void noBtnClicked(String btnName) {

            }
        });

        changeDialog.show();
    }

    //좌석 취소 체크 팝업
    private void showCancelDialog() {
        CheckDialog cancelDialog = new CheckDialog(this.getContext(), R.drawable.ic_error_48px, "예약 취소", "이미 예약한 본인 좌석입니다\n좌석을 취소하시겠습니까?");

        cancelDialog.setDialogListener(new CheckDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {
                retrofitAPI.reqCancelSeat(Employee.getInstance().getEmpId().toString()).enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        Employee data = response.body();
                        if(data.getResultCode() != null && !data.getResultCode().equals("") && data.getResultCode().equals("S101")) {
                            Employee.getInstance().setSeatId("");
                            // TODO: reqFloorSeat 인자 값 적용
                            reqFloorSeat(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {

                    }
                });
            }

            @Override
            public void noBtnClicked(String btnName) {

            }
        });

        cancelDialog.show();
    }

    // 좌석 예약 불가 팝업 커스텀
    private void showOccupiedDialog() {
        ConfirmDialog occupiedDialog = new ConfirmDialog(this.getContext(), R.drawable.ic_do_not_disturb_on_total_silence_48px, "예약 불가", "이미 예약된 좌석입니다");

        occupiedDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {

            }
        });

        occupiedDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
