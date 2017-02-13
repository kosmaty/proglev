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

oauth2Client.refreshAccessToken(function(err, tokens) {});


var readableStream = fs.createReadStream('proglev/client/target/proglev-0.0.1-SNAPSHOT.jar');
drive.files.update({
        fileId: '0B4sC0ym0PoYTT2duenNDTGZFMW8',
        resource: {
            name: 'proglev.jar',
            mimeType: 'application/octet-stream'
        },
        media: {
            mimeType: 'application/octet-stream',
            body: readableStream
        }
    }, function onFileCreated(fileResource){}
);