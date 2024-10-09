import React, { useEffect } from 'react'
import axios from 'axios';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
const SOCKET_URL = `${process.env.REACT_APP_API_BASE_URL}/ws-message`;
let subscription;
let startTime = 0; // Use performance.now() for more precision
        let endTime = 0;
const SocketTest = () => {

    // useEffect(async () => {
    //      startTime = performance.now(); // Use performance.now() for more precision
    //      endTime = 0;
    //     try {
    //         // Make the request to your backend API
    //         for (let index = 0; index < 100; index++) {
    //             await axios.get(`${process.env.REACT_APP_API_BASE_URL}/test`); // Replace with your backend endpoint
    //         }

    //         // Record the end time
    //         endTime = performance.now();

    //         // Calculate latency
    //         const latency = endTime - startTime;
    //         console.log("time for 100 requests: ", latency);
    //     } catch (error) {
    //         console.error('Error measuring latency:', error);
    //     }
    // }, []);

    useEffect(() => {

        // let startTime = 0; // Use performance.now() for more precision
        // let endTime = 0;


        const socket1 = new SockJS(SOCKET_URL);
        const client = Stomp.over(socket1);

        client.connect(
            { "Authorization": 'Bearer ' + localStorage.getItem('token') }, // Pass the JWT token as an Authorization header
            (frame) => {
                console.log('Connected:', frame);

                // Subscribe to the latency response
                subscription = client.subscribe('/topic/test-receive', (message) => {
                    endTime = performance.now(); // Record the time the message is received
                    //   const pingTime = parseFloat(message.body); // Parse the ping time sent from the client
                    //   const latency = endTime - pingTime;
                });

                // setClient(stompClient);
            }
        );

        console.log("hulala")
        startTime = performance.now();
        for (let index = 0; index < 100; index++) {
            client.send("/app/test", {}, "");
        }

        setTimeout(() => { console.log("hulala") });

          return () => {
            subscription.unsubscribe();
          }
    }, [])

    return (
        <div>
            See logs.
        </div>
    );
};

export default SocketTest;
