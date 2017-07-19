
var STATIONS_ID = "#stations";
var ORIGIN_STATION_ID = "#origin_station";
var ESTIMATE_ENTRIES_ID = "#estimate_entries";
var ESTIMATE_DIV_ID = "#estimates_div";
var GETTING_ESTIMATES_TEXT = "#getting_estimates_text";
var BART_DIV_ID = "#bart-div";
var LOADING_DIV_ID = "#loading-div";
var ERROR_DIV_ID = "#error-div";
var DATA_ATTR_ESTIMATE_MINUTES = "data-minutes-remaining";
var INTERVAL_ESTIMATE_CLEAR_TIME_MS = 30000; // 30 seconds -- 30000
var INTERVAL_ESTIMATE_TIME_MS = 60000; // 60 seconds -- 60000
var TIME_REMAINING_NOW_TEXT = "now";
var previousStationAbbreviation = '';
var previousStationName = '';
var estimateCountDownIntervals = [];

$(document).ready(function() {

    $(BART_DIV_ID).hide();
    $(LOADING_DIV_ID).show();
    $(ERROR_DIV_ID).hide();
    $(ESTIMATE_DIV_ID).hide();
    getAndSetAllStations();
    setupInputListeners();
    console.log("bart-control js loaded");
});

function setupInputListeners() {
    $(STATIONS_ID).bind('input', refreshEstimates);
}

function getEstimatesForStation(stationAbbr, stationName) {
    
    $(ESTIMATE_ENTRIES_ID).empty();
    $(ESTIMATE_DIV_ID).hide();
    $(GETTING_ESTIMATES_TEXT).text("Getting estimates for "+stationName);

    $.ajax({
        type: "GET",
        url: "api/estimates?station="+stationAbbr,
        success: function(data) {
            onNewEstimatesReturned(stationAbbr, data);
        },
        error: function(error) {
            onError(error);
        }
    })
}

function onNewEstimatesReturned(stationAbbr, estimates) {

    if ( stationAbbr != previousStationAbbreviation) {
        return; // make sure the same station is still selected
    }
    $(GETTING_ESTIMATES_TEXT).text("");
    $(ESTIMATE_ENTRIES_ID).empty();

    var estimatesForMinutes = [];

    if ( estimateCountDownIntervals.length > 0 ) {
        var index;
        for ( index = 0; index < estimateCountDownIntervals.length; index++) {
            clearInterval(estimateCountDownIntervals[index]);
        }
    }
    estimateCountDownIntervals = [];

    if ( estimates && estimates.entries && estimates.entries.length > 0 ) {
        // Need to sort asc for the time incoming
        // {"train_name":"SFO/Millbrae","minutes":2,"direction":"South","hex_color":"#ffff33"}
        estimates.entries.forEach(function (est) {
            if ( estimatesForMinutes.length === 0) {
                estimatesForMinutes.push(est); // just add it
            } else {
                // find where to insert this time
                var i;
                var insertIndex = estimatesForMinutes.length; // by default, set the insert to be last
                for ( i = 0; i < estimatesForMinutes.length; i++) {
                    var arrayEst = estimatesForMinutes[i];
                    if ( est.minutes <= arrayEst.minutes) {
                        insertIndex = i;
                        break;
                    }
                }
                estimatesForMinutes.splice(insertIndex, 0, est);
            }
        });
    }

    var idCounter = 0;
    if ( estimatesForMinutes.length > 0 ) {
        $(ESTIMATE_DIV_ID).show();
        // now just print all the information
        $(ORIGIN_STATION_ID).text(estimates.origin_station + ' Station');
        estimatesForMinutes.forEach(function (est) {
            idCounter++;
            var minutesRemaining = est.minutes;
            var timeRemainingText = est.minutes;
            if ( !minutesRemaining || minutesRemaining == 0) {
                timeRemainingText = TIME_REMAINING_NOW_TEXT;
                minutesRemaining = 0;
            } else {
                timeRemainingText = getMinutesRemainingTest(minutesRemaining);

            }
            $(ESTIMATE_ENTRIES_ID).append(
                '<div id=est-remaining-div-'+idCounter+' class="well well-lg bart-well" style="border-color: '+est.hex_color+'">'+est.train_name+' train arrives in ~<strong '+DATA_ATTR_ESTIMATE_MINUTES+'="'+minutesRemaining+'" id="est-remaining-text-'+idCounter+'">'+timeRemainingText+'</strong></div>'
            );
        });
    } else {
        $(GETTING_ESTIMATES_TEXT).text("There's no estimates available right now");
    }

    var counter;
    for ( counter = 1; counter <= idCounter; counter++) {
        countDownMinutesForId(counter);
    }
}

function getMinutesRemainingTest(minutesRemaining) {
    return minutesRemaining+' minutes';
}

function countDownMinutesForId(index) {

    var textElement = $("#est-remaining-text-"+index);
    var minutesRemaining = textElement.attr(DATA_ATTR_ESTIMATE_MINUTES);
    if ( minutesRemaining <= 0 ) {
        removeEstimateDiv(index);
        return; // nothing else to do
    }
    var newEstimateInterval = setInterval(function () {
        var newText;
        var textElement = $("#est-remaining-text-"+index);
        var minutesRemaining = textElement.attr(DATA_ATTR_ESTIMATE_MINUTES);
        var newMinutesRemaining = minutesRemaining - 1;
        if ( newMinutesRemaining > 0 ) {
            newText = getMinutesRemainingTest(newMinutesRemaining);
            textElement.attr(DATA_ATTR_ESTIMATE_MINUTES, newMinutesRemaining);
        } else {
            newText = TIME_REMAINING_NOW_TEXT;
            clearInterval(newEstimateInterval);
            var indexEst = estimateCountDownIntervals.indexOf(newEstimateInterval);
            if ( indexEst > -1 ) {
                estimateCountDownIntervals.splice(indexEst, 1);
            }
            removeEstimateDiv(index);
        }
        textElement.text(newText);

    }, INTERVAL_ESTIMATE_TIME_MS);

    estimateCountDownIntervals.push(newEstimateInterval);
}

function removeEstimateDiv(index) {
    setTimeout(function () {
        $("#est-remaining-div-"+index).hide(); // Hide the div the holds the text
        if ( previousStationAbbreviation && estimateCountDownIntervals.length < 1 ) {
            // get more estimates
            console.log("Looks like we have removed all estimates - so let's get more");
            getEstimatesForStation(previousStationAbbreviation, previousStationName);
        }

    }, INTERVAL_ESTIMATE_CLEAR_TIME_MS);
}


function getAndSetAllStations() {

    $.ajax({
        type: "GET",
        url: "api/stations",
        success: function(data) {
            refreshStations(data);
        },
        error: function(error) {
            onError(error);
        }
    })

}

function refreshStations(stations) {

    $(BART_DIV_ID).show();
    $(LOADING_DIV_ID).hide();

    if ( stations && stations.length > 0 ) {
        $(STATIONS_ID).append('<option disabled selected value> -- select a station -- </option>');
        stations.forEach(function (station) {
            $(STATIONS_ID).append($("<option />").val(station.abbreviation).text(station.name));
        });
    } else {
        console.log("No stations received - not able to refresh stations list")
    }
}

function refreshEstimates() {

    var stationName = $(STATIONS_ID+" option:selected").text();
    var stationAbbreviation = $(STATIONS_ID).val();
    if ( !stationAbbreviation || !stationName ) {
        return;
    }
    if ( stationAbbreviation === previousStationAbbreviation) {
        return;
    }
    if ( stationAbbreviation ) {
        console.log("Getting estimates for station [" + stationName + "] with abbreviation [" + stationAbbreviation + "] selected");
        previousStationAbbreviation = stationAbbreviation;
        previousStationName = stationName;
        getEstimatesForStation(stationAbbreviation, stationName);
    }
}

function onError(error) {

    $(BART_DIV_ID).hide();
    $(LOADING_DIV_ID).hide();
    $(ERROR_DIV_ID).show();
    console.log("An error occurred: "+error);
}
