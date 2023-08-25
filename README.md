# demo
test by aeris

This API has 4 endpoints
a. /get-info, returns the NetCDF detailed information.
b. /get-data, params to include time index and z index, returns a JSONArray that
includes x, y, and concentration data that match the params.
c. /get-image, params to include time index and z index, incomplete, returns png visualization of
concentration.
d. /get-vars returns the variables and their descriptions.   I included this for debugging purposes.
