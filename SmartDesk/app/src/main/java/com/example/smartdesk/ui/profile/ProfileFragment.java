package com.example.smartdesk.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.databinding.FragmentProfileBinding;
import com.example.smartdesk.ui.dialog.ChangePasswordDialog;
import com.example.smartdesk.ui.dialog.CustomDialog;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        // 카메라 접근
//        int permission = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
//        if (permission == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 0);
//        } else {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, 1);
//        }


        TextView change_desk = root.findViewById(R.id.change_desk);
        TextView desk_height = root.findViewById(R.id.desk_height);
        ImageView info = root.findViewById(R.id.info);
        TextView change_pw = root.findViewById(R.id.change_pw);

        // 비밀번호 변경 클릭 시, 페이지 이동
        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog.getInstance(requireActivity()).showChangePasswordDialog();
            }
        });

        // 책상 높이 버튼 클릭 시, 팝업 띄우기
        change_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.getInstance(requireActivity()).showCheckDialog();
            }
        });
        desk_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.getInstance(requireActivity()).showCheckDialog();
            }
        });

        // 좌석 자동 예약 info 클릭 시, 팝업 띄우기
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.getInstance(requireActivity()).showConfirmDialog();
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 0) {
//            if (grantResults[0] == 0) {
//                Toast.makeText(requireContext(), "카메라 권한 승인 완료", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(requireContext(), "카메라 권한 승인 거절", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}



