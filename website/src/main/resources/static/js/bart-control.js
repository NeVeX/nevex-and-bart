
var STATIONS_ID = "#stations";
var ORIGIN_STATION_ID = "#origin_station";
var ESTIMATE_ENTRIES_ID = "#estimate_entries";

var previousStationAbbreviation = '';
var previousStationName = '';
var estimateCountDownIntervals = [];

$(document).ready(function() {
    getAndSetAllStations();
    setupInputListeners();
    console.log("bart-control js loaded");
});

function setupInputListeners() {
    $(STATIONS_ID).bind('input', refreshEstimates);
}

function getEstimatesForStation(stationAbbr, stationName) {

    $(ESTIMATE_ENTRIES_ID).empty();
    $(ORIGIN_STATION_ID).text("Getting estimates for "+stationName);

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
        // now just print all the information
        $(ORIGIN_STATION_ID).text(estimates.origin_station + ' Station');

        estimatesForMinutes.forEach(function (est) {

            var minutesRemaining = est.minutes;
            var minutesRemainingText = est.minutes;
            if ( !minutesRemaining || minutesRemaining == 0) {
                minutesRemainingText = ' is arriving</div>'
            } else {
                idCounter++;
                minutesRemainingText = ' arrives in ~<strong id="est-remaining-'+idCounter+'">'+minutesRemaining+'</strong> minutes</div>';
            }

            $(ESTIMATE_ENTRIES_ID).append(
                '<div class="well well-lg bart-well" style="border-color: '+est.hex_color+'">'+est.train_name+' train '+minutesRemainingText
            );

        });
    } else {
        $(ORIGIN_STATION_ID).text("There's no estimates available right now");
    }

    var counter;
    for ( counter = 1; counter <= idCounter; counter++) {
        countDownMinutesForId(counter);
    }

}

function countDownMinutesForId(index) {
    var newEstimateInterval = setInterval(function () {
        var element = $("#est-remaining-"+index);
        var minutesRemaining = element.text();
        var newText;
        if ( minutesRemaining > 0 ) {
            newText = minutesRemaining - 1;
        } else {
            newText = 'now';
            clearInterval(newEstimateInterval);
            var indexEst = estimateCountDownIntervals.indexOf(newEstimateInterval);
            if ( indexEst > -1 ) {
                estimateCountDownIntervals.splice(indexEst, 0);
            }
            setTimeout(function () {
                element.hide(); // hide the information
                if ( previousStationAbbreviation && estimateCountDownIntervals.length < 1 ) {
                    // get more estimates
                    console.log("Looks like we have removed all estimates - so let's get more");
                    getEstimatesForStation(previousStationAbbreviation, previousStationName);
                }

            }, 30000); // 30 seconds
        }
        element.text(newText);

    }, 60000); // every minute
    estimateCountDownIntervals.push(newEstimateInterval);
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
    console.log("An error occurred: "+error);
    alert("An error occurred");

}
