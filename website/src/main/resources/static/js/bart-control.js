
var STATION_SELECTION_ID = "#stationSelection";
var STATIONS_ID = "#stations";
var ORIGIN_STATION_ID = "#origin_station";
var ESTIMATE_ENTRIES_ID = "#estimate_entries";

var previousStationAbbreviation = '';
var estimateCountDownIntervals = [];

$(document).ready(function() {
    getAndSetAllStations();
    setupInputListeners();
    console.log("bart-control js loaded");
});

function setupInputListeners() {

    $(STATION_SELECTION_ID).keydown(function(event){
        var keyCode = (event.keyCode ? event.keyCode : event.which);
        if (keyCode == 13) { // Enter key
            refreshEstimates();
        }
    });
    $(STATIONS_ID).bind('input', refreshEstimates);
    $(STATION_SELECTION_ID).bind('input', refreshEstimates);
}

function getEstimatesForStation(stationAbbr) {

    $(ESTIMATE_ENTRIES_ID).empty();
    $(ORIGIN_STATION_ID).text("Getting estimates for "+stationAbbr);

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
    if ( stations ) {
        stations.forEach(function (station) {
            $(STATIONS_ID).append('<option data-abbr="'+station.abbreviation+'" value="'+station.name+'">');
        });
    } else {
        console.log("No stations received - not able to refresh stations list")
    }
}

function refreshEstimates() {

    // Get what is selected
    var stationSelected = $(STATION_SELECTION_ID).val();
    if ( !stationSelected ) {
        return;
    }
    // Get our special data value (abbr)
    var stationAbbreviation = $(STATIONS_ID).find('[value="' + stationSelected + '"]').attr('data-abbr');
    // Nothing to do if they just clicked the same station again
    if ( stationAbbreviation === previousStationAbbreviation) {
        return;
    }
    if ( stationAbbreviation ) {
        console.log("Getting estimates for station [" + stationSelected + "] with abbreviation [" + stationAbbreviation + "] selected");
        previousStationAbbreviation = stationAbbreviation;
        getEstimatesForStation(stationAbbreviation);
    }
}

function onError(error) {
    console.log("An error occurred: "+error);
    alert("An error occurred");

}
