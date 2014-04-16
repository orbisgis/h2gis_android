cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/org.ogis.h2gis/www/h2gis.js",
        "id": "org.ogis.h2gis.H2GIS",
        "clobbers": [
            "h2gis"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "org.ogis.h2gis": "0.1.0"
}
// BOTTOM OF METADATA
});