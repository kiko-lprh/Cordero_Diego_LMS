# Library Management System

Author: Diego Cordero<br><br>
Course: CEN 3024 - Software Development 1<br><br>
Date: August 31, 2023

## Description

This Library Management System is designed to manage a library’s book collection. It provides a user-friendly command-line interface (CLI) that allows users to perform various operations on the book collection. <br><br>
**Note**: The CLI will be replaced by a more user-friendly GUI in the future.

## Features

### 1. Add Books

The application reads book information from a file, including barcode, title, and author, and adds them to the library database.

### 2. Delete Books

There are two options for deleting books:
- **Delete by Barcode**: Remove a book from the collection by typing its barcode number.
- **Delete by Title**: Remove books with a specific title. If there are multiple books with the same title, select which one to delete by typing its barcode number.

### 3. Check Books In and Out

- **Check In**: Mark a book as "available" after it has been returned. If the book is not currently checked out, the system displays an error message.
- **Check Out**: Borrow an available book. The system sets the due date for one month from the current date. If a book is already checked out, the system displays an error message.

### 4. Print Book Collection

Neatly prints all the books in the collection. Displays detailed information about each book.

## Usage

1. **Clone the Repository**: Start using the project by cloning the repository:

   ```bash
   git clone https://github.com/kiko-lprh/Cordero_Diego_LMS.git
