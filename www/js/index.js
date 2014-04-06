/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
        alert("TEST1");
        app.query();
        alert("TEST2");
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    },
    query: function() {
        var query1="CREATE TABLE ROADS (the_geom MULTILINESTRING, speed_limit INT)";
        var query2="INSERT INTO ROADS VALUES ('MULTILINESTRING((15 5, 20 6, 25 7))', 80)";
        var query3="INSERT INTO ROADS VALUES ('MULTILINESTRING((20 6, 21 15, 21 25))', 50)";
        var query4="SELECT SUM(ST_LENGTH(the_geom)) total_length from ROADS";
        var success1= function() {alert("success!");};
        var error = function(message) { alert("Oopsie! " + message); };
        var success2 = function(json) { alert( json); };

        alert("test1.5");
        h2gis.query(query1,success1,error);
        alert("test1.75");
        h2gis.query(query2,success1,error);
        h2gis.query(query3,success1,error);
        h2gis.query(query4,success2,error);

    }

};
