package com.example.smartdesk.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartdesk.R;
import com.example.smartdesk.databinding.FragmentProfileBinding;
import com.example.smartdesk.ui.dialog.ConfirmDialog;

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
        TextView change_option = root.findViewById(R.id.change_option);
        ImageView info = root.findViewById(R.id.info);

        // 책상 높이 버튼 클릭 시, 팝업 띄우기
        change_desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.getInstance(requireActivity()).showCheckDialog();
            }
        });
        desk_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.getInstance(requireActivity()).showCheckDialog();
            }
        });
        // 좌석 자동 예약 클릭 시, info 띄우기
        change_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.getInstance(requireActivity()).showConfirmDialog();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.getInstance(requireActivity()).showConfirmDialog();
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(requireContext(), "카메라 권한 승인 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "카메라 권한 승인 거절", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



