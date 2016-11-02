package cubesystem.kurimabank.model;

/**
 * Created by kurimabank-kurima on 2016/03/22.
 */
public class ActionDelay implements Action {
    private String mTitle;
    private String mComingTime;

    public ActionDelay (String title, String comingTime){
        mTitle = title;
        mComingTime = comingTime;
    }

    public String getmComingTime() {
        return mComingTime;
    }

    public void setmComingTime(String mComingTime) {
        this.mComingTime = mComingTime;
    }

    @Override
    public String getSummary() {
        return String.format("%sします。\n出社予定は%sになります。", mTitle, mComingTime);
    }
}
