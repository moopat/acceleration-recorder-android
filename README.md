# Acceleration Recorder for Android
This is the Android companion app for the [corresponding Pebble watchapp](https://github.com/moopat/acceleration-recorder-pebble). Acceleration data that is sent from the watch to the smartphone is persisted in this app and can either be exported as CSV or uploaded to a server. 

## Server Upload
The name of the server is configured in `Config.java`. As minimum requirement it should have a `multistore.php` file which accepts requests of the following format. The following contents are sent to the server via POST in the `request` field:

```json
 {
    "request":{
       "sampleRate":50,
       "sampleBatches":[
          {
             "timestamp":"2015-09-01T23:32:14.800",
             "hash":"dcb5500de1b922b98f6eee27265a5c397dd716e2c4b803e2ad63259e9d240085",
             "samples":[
                {
                   "v":992,
                   "z":0,
                   "y":0,
                   "x":0
                },
                {
                   "v":1016,
                   "z":0,
                   "y":0,
                   "x":0
                }
             ]
          }
       ],
       "datasetName":"set-532acd9c"
    }
 }
```
The request object holds a `datasetName` which is used to group samples that belong together and the `sampleRate` in Hz at which the samples were obtained. The request can hold an arbitrary number of sample batches. A sample batch consists of all samples that are delivered from the watchapp in a single AppMessage. The number has to be set in the watchapp as well as in `Config.NUMBER_SAMPLES`. A single sample consists of the Euclidean norm `v` (which is acquired by calculated the square root of x*x + y*y + z*z) and the values of the three axes of the accelerometer. Since x, y and z are not reported to the smartphone they are empty. A `sampleBatch` also holds the time at which is was received on the phone and a hash which is a unique identifier.

After successful transmission to the server the samples are deleted from the phone.

## CSV Export
There is an option in the menu to export all data as CSV. The export does not remove the data from the phone. The file is stored to the `Downloads` folder. If you cannot see it after mounting the device on your computer please open the Downloads folder using a file browser on your smartphone.

If you only need CSV export for the last few samples consider using [acceleration-stream-android](https://github.com/moopat/acceleration-stream-android) of which
this project is a fork of.

## Remote Control
Pro-Tip: By sending a text message containing only `PBL-GO` or `PBL-UPLOAD` to the smartphone you can launch the app or start uploading data to the server.

## License
```
The MIT License (MIT)

Copyright (c) 2015 Markus Deutsch

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
