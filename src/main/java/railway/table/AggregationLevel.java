package railway.table;

import java.util.function.Function;
import railway.model.DelayRecord;

public enum AggregationLevel {

    TRAIN_SERVICE("Train Service", DelayRecord::getTrainNumber),
    SERVICE("Service", DelayRecord::getService),
    COMPANY("Company", DelayRecord::getCompany),
    STATION("Station", DelayRecord::getStationName);

    public final Function<DelayRecord, String> keyExtractor;
    public final String kind;

    AggregationLevel(String kind, Function<DelayRecord, String> keyExtractor) {
    	this.kind = kind;
        this.keyExtractor = keyExtractor;
    }
}