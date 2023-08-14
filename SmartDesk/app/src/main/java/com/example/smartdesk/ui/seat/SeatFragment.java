package com.example.smartdesk.ui.seat;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.Model.ReqEmployee;
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

public class SeatFragment extends Fragment implements CheckDialog.CustomDialogInterface, ConfirmDialog.CustomDialogInterface {
    private final String TAG = "SeatFragment";
    private FragmentSeatBinding binding;

    Retrofit retrofit = RetrofitClient.getClient();
    RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    private Spinner floorSpinner;

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
        gridView.setAdapter(adapter);

        floorSpinner = root.findViewById(R.id.floorSpinner);
        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String selectedValue = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, (i + 2) + "층 선택");
                getFloorSeat(i + 2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //reqFloorSeat(2);

        //reqFloorSeat(2); // 초기값은 2층(0번 인덱스)

//        recyclerView = root.findViewById(R.id.grid_recyclerview);
//        adapter = new RecyclerViewAdapter(getContext(), seatList);
//
//        layoutManager = new GridLayoutManager(getContext(), 2);
//
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void okBtnClicked(String btnName) {

    }

    @Override
    public void noBtnClicked(String btnName) {

    }

    @Override
    public void btnClicked(String btnName) {

    }

    // floor: [2, 3, 4]
    public void getFloorSeat(int floor) {
        Log.d(TAG, "reqFloorSeat() Start");
        adapter.clearItems();
        adapter.notifyDataSetChanged();

        Log.d(TAG, "reqFloorSeat() init" + Integer.toString(floor));

        retrofitAPI.reqFloorSeat(floor).enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "reqFloorSeat() retrofit done");

                    List<Seat> data = response.body();

                    int sz = data.size();
                    Log.d(TAG, "좌석 size: " + sz);
                    for(int i=0;i<sz;i++){
                        Seat dSeat= data.get(i);
                        SeatItem cSeat = new SeatItem();
                        //Boolean isOnline = dSeat.getStatus().equals("false") ? false : true;
                        cSeat.setSeatId(dSeat.getSeatId());
                        cSeat.setNickname(dSeat.getNickname());
                        cSeat.setTeamName(dSeat.getTeamName());
                        cSeat.setStatus(dSeat.getStatus());

                        adapter.addItem(cSeat);
                        Log.d(TAG, cSeat.getSeatId());
                        //Log.d(TAG, cSeat.getSeatId() + ", " + cSeat.getNickname() + ", " + cSeat.getTeamName() + ", " + cSeat.getStatus());
                    }

                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "reqFloorSeat is not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {
                Log.d(TAG, "reqFloorSeat is failed" + t.getMessage());
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
                    showChangeDialog(item);
                } else { // 3-2) 점유된 좌석인 경우
                    // 예약된 좌석이라고 안내!
                    showOccupiedDialog();
                }
            }
        } else { // 1-2) 사원이 좌석 예약을 안 한 경우
            if(item.getNickname() == null || item.getNickname().isEmpty()) { // 2-1) 공석인 경우
                // 예약 할래?
                showReserveDialog(item.getSeatId());
            } else { // 2-2) 점유된 좌석인 경우
                // 예약된 좌석이라고 안내!
                showOccupiedDialog();
            }
        }
    }

    // 예약 요청 팝업
    public void showReserveDialog(String seatId) {
        CheckDialog reserveDialog = new CheckDialog(this.getContext(), R.drawable.ic_error_48px, "예약 요청", seatId + " 좌석으로 예약하시겠습니까?");
        reserveDialog.setDialogListener(new CheckDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {
                ReqEmployee reqEmp = new ReqEmployee();
                reqEmp.setEmpId(Employee.getInstance().getEmpId());
                reqEmp.setSeatId(seatId);
                retrofitAPI.reqReserveSeat(reqEmp).enqueue(new Callback<ReqEmployee>() {
                    @Override
                    public void onResponse(Call<ReqEmployee> call, Response<ReqEmployee> response) {
                        if (response != null && response.isSuccessful()) {
                            // Response 값을 사용
                            ReqEmployee data = response.body();
                            ConfirmDialog confirmDialog = new ConfirmDialog(getContext());

                            if(data.getResultCode() == null || data.getResultCode().equals("")) {
                                Log.d(TAG, "reqReserveSeat() resultCode 없음");
                                return;
                            }
                            else if(data.getResultCode().equals("S101")) {
                                Employee.getInstance().setSeatId(data.getSeatId());
                                Log.d(TAG, "신규 좌석 예약: " + Employee.getInstance().getSeatId());
                                confirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_check_circle_48px, "예약 확인", "예약이 확정되었습니다.");
                            }
                            else if(data.getResultCode().equals("S201")) {
                                confirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_do_not_disturb_on_total_silence_48px, "예약 불가", "예약한 좌석이 이미 있습니다");
                            }
                            else if(data.getResultCode().equals("S202")) {
                                confirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_do_not_disturb_on_total_silence_48px, "예약 불가", "좌석은 출근 후 예약 가능합니다");
                            }
                            else if(data.getResultCode().equals("S203")) {
                                confirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_do_not_disturb_on_total_silence_48px, "예약 불가", "퇴근 후에는 예약이 불가합니다\n익일 좌석은 출근 후 예약 가능합니다");
                            }

                            confirmDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
                                @Override
                                public void btnClicked(String btnName) {
                                    getFloorSeat(floorSpinner.getSelectedItemPosition() + 2);
                                }
                            });

                            confirmDialog.show();
                        } else {
                            // 에러 처리
                            if (response == null) Log.d(TAG, "showReserveDialog() is null");
                            if (!response.isSuccessful()) Log.d(TAG, "showReserveDialog() is not successful");
                        }
                    }

                    @Override
                    public void onFailure(Call<ReqEmployee> call, Throwable t) {
                        Log.d(TAG, "reqReserveSeat() 통신 실패");
                    }
                });
            }

            @Override
            public void noBtnClicked(String btnName) {

            }
        });

        reserveDialog.show();
    }

    //좌석 변경 체크 팝업
    public void showChangeDialog(SeatItem item) {
        CheckDialog changeDialog = new CheckDialog(this.getContext(), R.drawable.ic_error_48px, "예약 변경", "이미 예약된 좌석이 있습니다\n좌석을 변경하시겠습니까?");

        changeDialog.setDialogListener(new CheckDialog.CustomDialogInterface() {
            @Override
            public void okBtnClicked(String btnName) {
                ReqEmployee reqEmployee = new ReqEmployee();
                reqEmployee.setEmpId(Employee.getInstance().getEmpId());
                reqEmployee.setSeatId(item.getSeatId());
                retrofitAPI.reqChangeSeat(reqEmployee).enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        Employee data = response.body();
                        if(data.getResultCode().equals("S101")) {
                            Employee.getInstance().setSeatId(data.getSeatId());

                            // 예약 변경 확인 팝업
                            ConfirmDialog confirmDialog = new ConfirmDialog(getContext(), R.drawable.ic_check_circle_48px, "예약 확인", "예약이 변경되었습니다.");

                            confirmDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
                                @Override
                                public void btnClicked(String btnName) {
                                    getFloorSeat(floorSpinner.getSelectedItemPosition() + 2);
                                }
                            });

                            confirmDialog.show();
                        } else if(data.getResultCode().equals("S201")) {
                            Log.d(TAG, "reqChangeSeat(): 이미 예약된 좌석입니다");
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
    public void showCancelDialog() {
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
                            getFloorSeat(floorSpinner.getSelectedItemPosition() + 2);
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
    public void showOccupiedDialog() {
        ConfirmDialog occupiedDialog = new ConfirmDialog(this.getContext(), R.drawable.ic_do_not_disturb_on_total_silence_48px, "예약 불가", "이미 예약된 좌석입니다");

        occupiedDialog.setDialogListener(new ConfirmDialog.CustomDialogInterface() {
            @Override
            public void btnClicked(String btnName) {

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
