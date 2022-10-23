package com.lzp.smp.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzp.smp.Entity.StudentEntity;
import com.lzp.smp.R;
import com.lzp.smp.util.GsonUtils;
import com.lzp.smp.util.SPUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.Inflater;

public class StudentActivity extends AppCompatActivity {
    private static final String SORT_ALL_DES = "SORT_ALL_DES";//总分降序
    private static final String SORT_ALL_ASC = "SORT_ALL_ASC";//总分升序
    private static final String SORT_C_DES = "SORT_C_DES";//C++降序
    private static final String SORT_C_ASC = "SORT_C_ASC";//C++升序
    private static final String SORT_ENGLISH_DES = "SORT_ENGLISH_DES";//英语降序
    private static final String SORT_ENGLISH_ASC = "SORT_ENGLISH_ASC";//英语升序
    private static final String SORT_MATH_DES = "SORT_MATH_DES";//数学降序
    private static final String SORT_MATH_ASC = "SORT_MATH_ASC";//数学升序
    private String sortCurrent;

    private RecyclerView rvStudent;
    private StudentAdapter studentAdapter;
    private ArrayList<StudentEntity> datas;
    //    private String[] array = {"从文件读取", "输出到文件", "按当前排序插入", "清空"};
    private String[] array = {"从文件读取", "按当前排序插入", "清空", "总分降序", "总分升序", "C++降序", "C++升序", "英语降序", "英语升序", "数学降序", "数学升序", "不及格人数统计"};
    private String[] arrayItem = {"删除", "修改"};

    private TextView m_tv_num;
    private TextView m_tv_name;
    private TextView m_tv_english;
    private TextView m_tv_math;
    private TextView m_tv_all;
    private Button btn_search;
    private Button btn_recovery;
    private EditText et_search;
    private ImageView m_iv_more;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initView();
        initData();
    }

    private void initView() {
        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);
        btn_recovery = findViewById(R.id.btn_recovery);
        m_tv_num = findViewById(R.id.tv_num);
        m_tv_name = findViewById(R.id.tv_name);
        m_tv_english = findViewById(R.id.tv_english);
        m_tv_math = findViewById(R.id.tv_math);
        m_tv_all = findViewById(R.id.tv_all);
        m_iv_more = findViewById(R.id.iv_more);
        rvStudent = findViewById(R.id.recycler_view);
    }

    private void initData() {
        datas = getNewest();
        initRecycler();
        sortCurrent = SORT_ALL_DES;
        updateRank();
        m_iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_search.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(StudentActivity.this, "请输入搜索条件", Toast.LENGTH_LONG).show();
                } else if (datas.size() == 0) {
                    Toast.makeText(StudentActivity.this, "请先添加学生数据再搜索", Toast.LENGTH_LONG).show();
                } else {
                    datas.clear();
                    ArrayList<StudentEntity> datasNewest = getNewest();
                    for (StudentEntity studentEntity : datasNewest) {
                        if (studentEntity.getNum().contains(str)) {
                            datas.add(studentEntity);
                        }
                    }
                    studentAdapter.notifyDataSetChanged();
                }
            }
        });
        btn_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                datas = getNewest();
                studentAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvStudent.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter();
        rvStudent.setAdapter(studentAdapter);
    }

    public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyHolder> {

        @Override
        public StudentAdapter.MyHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
            StudentAdapter.MyHolder holder = new StudentAdapter.MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(StudentAdapter.MyHolder holder, int position) {
            final StudentEntity studentEntity = datas.get(position);
            holder.tv_rank.setText(String.valueOf(studentEntity.getRang()));
            holder.tv_num.setText(studentEntity.getNum());
            holder.tv_name.setText(studentEntity.getName());
            holder.tv_c.setText(String.valueOf(studentEntity.getC()));
            holder.tv_english.setText(String.valueOf(studentEntity.getEnglish()));
            holder.tv_math.setText(String.valueOf(studentEntity.getMath()));
            holder.tv_all.setText(String.valueOf(studentEntity.getAll()));
            holder.tv_ave.setText(String.valueOf(studentEntity.getAve()));
            holder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoreItem(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            private TextView tv_rank;
            private TextView tv_num;
            private TextView tv_name;
            private TextView tv_c;
            private TextView tv_english;
            private TextView tv_math;
            private TextView tv_all;
            private TextView tv_ave;
            private ImageView iv_more;

            public MyHolder(View itemView) {
                super(itemView);
                tv_rank = itemView.findViewById(R.id.tv_rank);
                tv_num = itemView.findViewById(R.id.tv_num);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_c = itemView.findViewById(R.id.tv_c);
                tv_english = itemView.findViewById(R.id.tv_english);
                tv_math = itemView.findViewById(R.id.tv_math);
                tv_all = itemView.findViewById(R.id.tv_all);
                tv_ave = itemView.findViewById(R.id.tv_ave);
                iv_more = itemView.findViewById(R.id.iv_more);
            }
        }
    }

    public void showMore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0) {
                    readFile();
                } else if (pos == 1) {
                    studentInsertUpdate(false, 0, "", "", "", "", "");
                } else if (pos == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this)
                            .setTitle("清除数据不可恢复！！！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    datas.clear();
                                    save();
                                    studentAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                } else if (pos == 3) {
                    sortCurrent = SORT_ALL_DES;
                    updateRank();
                } else if (pos == 4) {
                    sortCurrent = SORT_ALL_ASC;
                    updateRank();
                } else if (pos == 5) {
                    sortCurrent = SORT_C_DES;
                    updateRank();
                } else if (pos == 6) {
                    sortCurrent = SORT_C_ASC;
                    updateRank();
                } else if (pos == 7) {
                    sortCurrent = SORT_ENGLISH_DES;
                    updateRank();
                } else if (pos == 8) {
                    sortCurrent = SORT_ENGLISH_ASC;
                    updateRank();
                } else if (pos == 9) {
                    sortCurrent = SORT_MATH_DES;
                    updateRank();
                } else if (pos == 10) {
                    sortCurrent = SORT_MATH_ASC;
                    updateRank();
                } else if (pos == 11) {
                    failNum();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void failNum() {
        View viewDia = LayoutInflater.from(StudentActivity.this).inflate(R.layout.dlg_fail_num, null);
        TextView tvC = viewDia.findViewById(R.id.tv_c);
        TextView tvEnglish = viewDia.findViewById(R.id.tv_english);
        TextView tvMath = viewDia.findViewById(R.id.tv_math);
        TextView tvAll = viewDia.findViewById(R.id.tv_all);

        int fC = 0, fE = 0, fM = 0, fA = 0;
        for (StudentEntity studentEntity : datas) {
            if (studentEntity.getC() < 60) {
                fC++;
            }
            if (studentEntity.getEnglish() < 60) {
                fE++;
            }
            if (studentEntity.getMath() < 60) {
                fM++;
            }
        }
        fA = fC + fE + fM;
        tvC.setText(String.valueOf(fC));
        tvEnglish.setText(String.valueOf(fE));
        tvMath.setText(String.valueOf(fM));
        tvAll.setText(String.valueOf(fA));

        AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this)
                .setView(viewDia)
                .setTitle("不及格人数统计")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    // 插入学生信息
    private void studentInsertUpdate(boolean isUpdate,int position, String num, String name, String c, String english, String math) {
        View viewDia = LayoutInflater.from(StudentActivity.this).inflate(R.layout.edit_student, null);
        TextView tvRank = viewDia.findViewById(R.id.tv_rank);
        tvRank.setVisibility(View.GONE);
        EditText etNum = viewDia.findViewById(R.id.tv_num);
        etNum.setHint("学号");
        EditText etName = viewDia.findViewById(R.id.tv_name);
        etName.setHint("姓名");
        EditText etC = viewDia.findViewById(R.id.tv_c);
        etC.setHint("C++成绩");
        EditText etEnglish = viewDia.findViewById(R.id.tv_english);
        etEnglish.setHint("英语成绩");
        EditText etMath = viewDia.findViewById(R.id.tv_math);
        etMath.setHint("数学成绩");
        TextView tvAll = viewDia.findViewById(R.id.tv_all);
        tvAll.setVisibility(View.GONE);
        TextView tvAve = viewDia.findViewById(R.id.tv_ave);
        tvAve.setVisibility(View.GONE);

        etC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cStr = etC.getText().toString();
                String englishStr = etEnglish.getText().toString();
                String mathStr = etMath.getText().toString();
                double c = Double.valueOf(TextUtils.isEmpty(cStr) ? "0" : cStr);
                double english = Double.valueOf(TextUtils.isEmpty(englishStr) ? "0" : englishStr);
                double math = Double.valueOf(TextUtils.isEmpty(mathStr) ? "0" : mathStr);
                double sum = c + english + math;
                double ave = sum / 3;
                tvAll.setText(String.valueOf(sum));
                tvAve.setText(String.valueOf(ave));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEnglish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cStr = etC.getText().toString();
                String englishStr = etEnglish.getText().toString();
                String mathStr = etMath.getText().toString();
                double c = Double.valueOf(TextUtils.isEmpty(cStr) ? "0" : cStr);
                double english = Double.valueOf(TextUtils.isEmpty(englishStr) ? "0" : englishStr);
                double math = Double.valueOf(TextUtils.isEmpty(mathStr) ? "0" : mathStr);
                double sum = c + english + math;
                double ave = sum / 3;
                tvAll.setText(String.valueOf(sum));
                tvAve.setText(String.valueOf(ave));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMath.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cStr = etC.getText().toString();
                String englishStr = etEnglish.getText().toString();
                String mathStr = etMath.getText().toString();
                double c = Double.valueOf(TextUtils.isEmpty(cStr) ? "0" : cStr);
                double english = Double.valueOf(TextUtils.isEmpty(englishStr) ? "0" : englishStr);
                double math = Double.valueOf(TextUtils.isEmpty(mathStr) ? "0" : mathStr);
                double sum = c + english + math;
                double ave = sum / 3;
                tvAll.setText(String.valueOf(sum));
                tvAve.setText(String.valueOf(ave));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNum.setText(num);
        etName.setText(name);
        etC.setText(c);
        etEnglish.setText(english);
        etMath.setText(math);

        AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this)
                .setView(viewDia)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(etNum.getText().toString())) {
                            Toast.makeText(StudentActivity.this, "请输入学号", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (TextUtils.isEmpty(etName.getText().toString())) {
                            Toast.makeText(StudentActivity.this, "请输入姓名", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!isUpdate) {
                            for (StudentEntity studentEntity : datas) {
                                if (TextUtils.equals(studentEntity.getNum(), etNum.getText().toString())) {
                                    Toast.makeText(StudentActivity.this, "已有此学号，请重新填写", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                        StudentEntity studentEntity = new StudentEntity();
//                        studentEntity.setRang(Integer.parseInt(tvRank.getText().toString()));
                        studentEntity.setNum(etNum.getText().toString());
                        studentEntity.setName(etName.getText().toString());
                        studentEntity.setC(Float.parseFloat(etC.getText().toString()));
                        studentEntity.setEnglish(Float.parseFloat(etEnglish.getText().toString()));
                        studentEntity.setMath(Float.parseFloat(etMath.getText().toString()));
//                                    studentEntity.setAll(Float.valueOf(tvAll.getText().toString()));
//                                    studentEntity.setAve(Float.valueOf(tvAve.getText().toString()));
                        if (isUpdate) {
                            datas.remove(position);
                        }
                        datas.add(studentEntity);
                        save();
                        updateRank();
//                                    studentAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void showMoreItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        builder.setItems(arrayItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0) {
                    datas.remove(position);
                    save();
                    updateRank();
                } else if (pos == 1) {
                    StudentEntity data = datas.get(position);
                    studentInsertUpdate(true, position, data.getNum(), data.getName(),
                            String.valueOf(data.getC()), String.valueOf(data.getEnglish()), String.valueOf(data.getMath()));
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void save() {
        String studentsStr = GsonUtils.gsonToString(datas);
        SPUtil.getInstance(this).setValue(SPUtil.KEY_STUDENT, studentsStr);
    }

    public ArrayList<StudentEntity> getNewest() {
        String studentsStr = SPUtil.getInstance(this).getValue(SPUtil.KEY_STUDENT);
        ArrayList<StudentEntity> datas = (ArrayList<StudentEntity>) GsonUtils.jsonToList(studentsStr, StudentEntity.class);
        return datas;
    }

    public void readFile() {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setNum("20220101");
        studentEntity.setName("张三");
        studentEntity.setEnglish(50);
        studentEntity.setMath(75);
        studentEntity.setC(78.5f);
        float sum = studentEntity.getC() + studentEntity.getEnglish() + studentEntity.getMath();
        float ave = sum / 3;
        studentEntity.setAll(sum);
        studentEntity.setAve(ave);

        StudentEntity studentEntity1 = new StudentEntity();
        studentEntity1.setNum("20220102");
        studentEntity1.setName("李四");
        studentEntity1.setEnglish(80);
        studentEntity1.setMath(77);
        studentEntity1.setC(73.5f);
        float sum1 = studentEntity1.getC() + studentEntity1.getEnglish() + studentEntity1.getMath();
        float ave1 = sum1 / 3;
        studentEntity1.setAll(sum1);
        studentEntity1.setAve(ave1);

        StudentEntity studentEntity2 = new StudentEntity();
        studentEntity2.setNum("20220103");
        studentEntity2.setName("王五");
        studentEntity2.setEnglish(67);
        studentEntity2.setMath(45);
        studentEntity2.setC(73.5f);
        float sum2 = studentEntity2.getC() + studentEntity2.getEnglish() + studentEntity2.getMath();
        float ave2 = sum2 / 3;
        studentEntity2.setAll(sum2);
        studentEntity2.setAve(ave2);

        StudentEntity studentEntity3 = new StudentEntity();
        studentEntity3.setNum("20220104");
        studentEntity3.setName("赵六");
        studentEntity3.setEnglish(89);
        studentEntity3.setMath(96);
        studentEntity3.setC(73.5f);
        float sum3 = studentEntity3.getC() + studentEntity3.getEnglish() + studentEntity3.getMath();
        float ave3 = sum3 / 3;
        studentEntity3.setAll(sum3);
        studentEntity3.setAve(ave3);

        datas.add(studentEntity);
        datas.add(studentEntity1);
        datas.add(studentEntity2);
        datas.add(studentEntity3);
        save();
        updateRank();
    }

    // 总分 降序
    private void getAllDES() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getAll() > o2.getAll()) return -1;
                else if(o1.getAll() < o2.getAll()) return 1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // 总分 升序
    private void getAllASC() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getAll() > o2.getAll()) return 1;
                else if(o1.getAll() < o2.getAll()) return -1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // C++ 降序
    private void getCDES() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getC() > o2.getC()) return -1;
                else if(o1.getC() < o2.getC()) return 1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // C++ 升序
    private void getCASC() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getC() > o2.getC()) return 1;
                else if(o1.getC() < o2.getC()) return -1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // 英语 降序
    private void getEnglishDES() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getEnglish() > o2.getEnglish()) return -1;
                else if(o1.getEnglish() < o2.getEnglish()) return 1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // 英语 升序
    private void getEnglishASC() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getEnglish() > o2.getEnglish()) return 1;
                else if(o1.getEnglish() < o2.getEnglish()) return -1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // 数学 降序
    private void getMathDES() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getMath() > o2.getMath()) return -1;
                else if(o1.getMath() < o2.getMath()) return 1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // 数学 升序
    private void getMathASC() {
        Collections.sort(datas, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getMath() > o2.getMath()) return 1;
                else if(o1.getMath() < o2.getMath()) return -1;
                else return 0;
            }
        });
        studentAdapter.notifyDataSetChanged();
    }

    // 增删改 更新名次和排序
    private void updateRank() {
        ArrayList<StudentEntity> datasNewest = getNewest();
        Collections.sort(datasNewest, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getAll() > o2.getAll()) return -1;
                else if(o1.getAll() < o2.getAll()) return 1;
                else return 0;
            }
        });
        for (int i = 0; i < datasNewest.size(); i++) {
            StudentEntity studentEntity = datasNewest.get(i);
            for (StudentEntity data : datas) {
                if (TextUtils.equals(studentEntity.getNum(), data.getNum())) {
                    data.setRang(i + 1);
                }
            }
        }
        switch (sortCurrent) {
            case SORT_ALL_DES:
                getAllDES();
                break;
            case SORT_ALL_ASC:
                getAllASC();
                break;
            case SORT_C_DES:
                getCDES();
                break;
            case SORT_C_ASC:
                getCASC();
                break;
            case SORT_ENGLISH_DES:
                getEnglishDES();
                break;
            case SORT_ENGLISH_ASC:
                getEnglishASC();
                break;
            case SORT_MATH_DES:
                getMathDES();
                break;
            case SORT_MATH_ASC:
                getMathASC();
                break;
            default:
                studentAdapter.notifyDataSetChanged();
        }
        save();
    }

    // 更新当前排序
    private void updatCurrentSort() {
        ArrayList<StudentEntity> datasNewest = getNewest();
        Collections.sort(datasNewest, new Comparator<StudentEntity>() {
            @Override
            public int compare(StudentEntity o1, StudentEntity o2) {
                if(o1.getAll() > o2.getAll()) return -1;
                else if(o1.getAll() < o2.getAll()) return 1;
                else return 0;
            }
        });
        for (int i = 0; i < datasNewest.size(); i++) {
            StudentEntity studentEntity = datasNewest.get(i);
            for (StudentEntity data : datas) {
                if (TextUtils.equals(studentEntity.getNum(), data.getNum())) {
                    data.setRang(i + 1);
                }
            }
        }
        studentAdapter.notifyDataSetChanged();
    }
}
