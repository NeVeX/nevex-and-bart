$(document).ready(function() {
    var previousStation = '';

    function getEstimates() {
        // Need the abbreviation
        var stationSelected = $('#stationSelection').val();
        if ( !stationSelected) { // empty or null
            return;
        }
        if ( stationSelected === previousStation) {
            return;
        }
        var allStations = $('#stations');
        var stationSelectedOption = $(allStations).find('option[value="' + stationSelected + '"]');
        var stationAbbreviation = stationSelectedOption.attr('data-value');
        if ( stationAbbreviation) {
            console.log("Getting estimates for station " + stationSelected + " with abbreviation " + stationAbbreviation + " selected");
            previousStation = stationSelected;
            // call the API
            var estimationApi = "estimates?station=" + stationAbbreviation;
            $("#bart_estimates").load(estimationApi); // using fragments - load the html directly
        }
    }

    $('#stationSelection').keydown(function(event){
        var keyCode = (event.keyCode ? event.keyCode : event.which);
        if (keyCode == 13) { // Enter key
            getEstimates();
        }
    });
    $('#stations').bind('input', getEstimates);
    $('#stationSelection').bind('input', getEstimates);
});
