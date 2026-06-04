const API_URL = "http://localhost:8080/api/rooms";

loadDashboard();

async function loadDashboard() {

    const response = await fetch(API_URL);
    const rooms = await response.json();

    document.getElementById("totalRooms").innerText = rooms.length;

    const available = rooms.filter(
        room => room.status?.toLowerCase() === "available"
    ).length;

    const occupied = rooms.filter(
        room => room.status?.toLowerCase() === "occupied"
    ).length;

    document.getElementById("availableRooms").innerText = available;
    document.getElementById("occupiedRooms").innerText = occupied;
}