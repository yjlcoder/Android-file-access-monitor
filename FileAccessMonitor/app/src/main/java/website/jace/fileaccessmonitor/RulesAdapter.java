package website.jace.fileaccessmonitor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class RulesAdapter extends ArrayAdapter<RuleModel> {
    private ArrayAdapter<AppIdModel> spinnerAdapater;
    private ArrayList<RuleModel> dataSet;
    private Context mContext;

    public RulesAdapter(@NonNull Context context, ArrayList<RuleModel> list) {
        super(context, 0, list);
        mContext = context;
        dataSet = list;

        spinnerAdapater = new ArrayAdapter<AppIdModel>(context, android.R.layout.simple_spinner_item);
        spinnerAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (AppIdModel appId: Data.getInstance().appIdModelList) {
            spinnerAdapater.add(appId);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.rule_row, parent, false);

        RuleModel rule = dataSet.get(position);
        Spinner spinner = listItem.findViewById(R.id.app_name_dropdown);
        spinner.setAdapter(spinnerAdapater);
        spinner.setSelection(Data.getInstance().appIdModelList.indexOf(rule.getAppId()));

        EditText path = listItem.findViewById(R.id.file_path);
        path.setText(rule.getPath());

        EditText realPath = listItem.findViewById(R.id.real_path);
        realPath.setText(rule.getRealPath());

        ImageButton button = listItem.findViewById(R.id.delete_rule);
        button.setOnClickListener(v -> {
            dataSet.remove(position);
            notifyDataSetChanged();
        });

        return listItem;
    }
}
