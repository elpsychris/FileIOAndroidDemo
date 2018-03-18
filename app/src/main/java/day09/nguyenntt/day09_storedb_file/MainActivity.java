package day09.nguyenntt.day09_storedb_file;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    private EditText edtName;
    private TextView txtResult;
    private static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        txtResult = findViewById(R.id.txtResult);

        InputStream is = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        String str = null;

        try {
            is = this.getResources().openRawResource(R.raw.data);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            while ((str = br.readLine()) != null) {
                edtName.setText(str);
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void clickToSaveExternal(View view) {
        String str = edtName.getText().toString();
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File directory = new File(realPath + "/MyFiles");
        System.out.println("[AAA]" + directory.getAbsolutePath());
        directory.mkdir();
        File file = new File(directory, "myfile.txt");

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {

            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write(str);
            osw.flush();
            txtResult.setText("Save SDCard success");
            edtName.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //tu dong ket noi
            try {
                if (osw != null) {
                    osw.close();
                }

                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex) {

            }
        }
    }

    public void clickToLoadExternal(View view) {
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File directory = new File(realPath + "/MyFiles");
        directory.mkdir();
        File file = new File(directory, "myfile.txt");

        FileInputStream fis = null;
        InputStreamReader isr = null;

        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);

            char[] buffer = new char[READ_BLOCK_SIZE];
            String a = "Value: ";
            int charRead;
            while ((charRead = isr.read(buffer)) > 0) {
                String reading = String.copyValueOf(buffer, 0, charRead);
                a += reading;
                buffer = new char[READ_BLOCK_SIZE];
            }
            edtName.setText(a);
            txtResult.setText("Load SDCard successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //tu dong ket noi

            try {
                if (isr != null) {
                    isr.close();
                }

                if (fis != null) {
                    fis.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickToSaveInternal(View view) {
        String str = edtName.getText().toString();
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = openFileOutput("myfile.txt", MODE_WORLD_READABLE);
            osw = new OutputStreamWriter(fos);
            osw.write(str);
            osw.flush();
            edtName.setText("");
            txtResult.setText("Save Internal successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //tu dong ket noi

            try {
                if (osw != null) {
                    osw.close();
                }

                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clickToLoadInternal(View view) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            fis = openFileInput("myfile.txt");
            isr = new InputStreamReader(fis);
            char[] buffer = new char[READ_BLOCK_SIZE];
            String s = "Value: ";

            int charRead;
            while ((charRead = isr.read(buffer)) > 0) {
                String reading = String.copyValueOf(buffer, 0, charRead);
                s += reading;
                buffer = new char[READ_BLOCK_SIZE];
            }
            edtName.setText(s);
            txtResult.setText("Load Internal successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }

                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void copyFile(String src, String dst) {
        File srcF = new File(src);
        File dstF = new File(dst);
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(srcF).getChannel();
            dstChannel = new FileOutputStream(dstF).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
                if (dstChannel != null) {
                    srcChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickToBackup(View view) {
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File data = Environment.getDataDirectory();
        String dstDir = realPath + "/MyFiles";
        File directory = new File(dstDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String dataPath = "/data/" + this.getPackageName() + "/files";
        // "/shared_prefs", "/databases"
        File dataDir = new File(data, dataPath);
        File[] fileList = dataDir.listFiles();
        String result = "Copy file: \n";
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                File f = fileList[i];
                result += f.getName() + "\n";
                copyFile(f.getAbsolutePath(), dstDir + "/" + f.getName());
            }
        } else {
            result += "No file";
        }
        txtResult.setText(result);
    }
}
