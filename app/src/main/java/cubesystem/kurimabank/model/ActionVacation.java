package cubesystem.kurimabank.model;

/**
 * Created by kurimabank-kurima on 2016/03/22.
 */
public class ActionVacation implements Action {
    private String mTitle;

    public ActionVacation(String title){
        mTitle = title;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public String getSummary() {
        return String.format("%sします。", this.mTitle);
    }
}
