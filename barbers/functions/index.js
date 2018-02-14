const functions = require('firebase-functions');

const admin = require('firebase-admin');
const uuid = require('uuid');
admin.initializeApp(functions.config().firebase);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.addMockData = functions.https.onRequest((req, res) =>{

    const shop = {
        id: uuid.v4(),
        name: "Chintu Barbar",
        phone: "+91-8894121222",
        address: "Huda Market, Sector 56, Gurugram, Haryana",
        seats: 1,
        likes: 72,
        sevices_m: [
            {
                name: "Hair Trimming",
                time_mins: 15,
                price: 70
            },
            {
                name: "Hair Cut",
                time_mins: 20,
                price: 100
            },
            {
                name: "Head Massage",
                time_mins: 30,
                price: 150
            },
            {
                name: "Shave",
                time_mins: 30,
                price: 80
            },
            {
                name: 'Beard Trim',
                time_mins: 20,
                price: 50
            }
        ],
        services_f: [
            {
                name: "Waxing",
                time_mins: 50,
                price: 250
            },
            {
                name: "Hair Cut",
                time_mins: 45,
                price: 150
            },
            {
                name: "Manicure",
                time_mins: 45,
                price: 200
            },
            {
                name: "Hair Colour",
                time_mins: 60,
                price: 200
            }
        ],
        reviews:[
            {
                id: uuid.v4(),
                text: "Good for ladies, but not so much for kids",
                timestamp: admin.database.ServerValue.TIMESTAMP
            },
            {
                id: uuid.v4(),
                text: "Massaage was not that great",
                timestamp: admin.database.ServerValue.TIMESTAMP
            }
        ],
        lat: "123123",
        loc: "234343",
        bookings: [
            {
                bookingId: uuid.v4(),
                date: '05-09-2017',
                endTime: '0900',
                startTime: '0800'
            }
        ]
    }


    admin.database().ref('/shops').push(shop)
        .then(snap=>{
            res.send(200, {status: true, message: "success"});
        }).catch(err=>{
            res.send(200, {status: false, message:"failed"});
        })
});
