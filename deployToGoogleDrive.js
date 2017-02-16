var google = require('googleapis');
var fs = require('fs');

var OAuth2 = google.auth.OAuth2;


var oauth2Client = new OAuth2(
    process.env.CLIENT_ID,
    process.env.CLIENT_SECRET,
    "http://localhost:5432"
);

oauth2Client.setCredentials({
    refresh_token: process.env.REFRESH_TOKEN,
});

var drive = google.drive({
    version: 'v3',
    auth: oauth2Client
});



var readableStream = fs.createReadStream('proglev/client/target/proglev.exe');
drive.files.update({
        fileId: '0B4sC0ym0PoYTUUx0STF0UHVIYVU',
        resource: {
            name: 'proglev.exe',
            mimeType: 'application/octet-stream'
        },
        media: {
            mimeType: 'application/octet-stream',
            body: readableStream
        }
    }, function onFileCreated(fileResource){}
);