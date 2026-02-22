# Quantity Measurement App – UC1 (Feet Equality)

### 📌 Overview

- This module checks whether two measurements given in feet are equal.
- It focuses on correct `object equality`, `safe floating-point comparison`, and clean OOP design.

### ⚙️ Use Case: UC1 – Feet Measurement Equality

- Accepts two numerical values in feet
- Compares them for equality
- Returns `true` if equal, otherwise false

### ⚙️ Key Implementation Points

- Uses a separate Feet class to represent a measurement
- Measurement value is `private` and `final` (immutable)
- `equals()` is overridden correctly
- `Double.compare()` is used instead of `==`
- Handles `null`, type mismatch, and same reference cases safely

---

# Quantity Measurement App – UC2 (Inches Equality)

### 📌 Overview

- This module checks whether two measurements given in **inches** are equal.
- It extends UC1 by supporting equality checks for inches while following the same design principles.

### ⚙️ Use Case: UC2 – Inches Measurement Equality

- Accepts two numerical values in inches
- Compares them for equality
- Returns `true` if equal, otherwise false

### ⚙️ Key Implementation Points

- Uses a separate **Inches** class to represent a measurement
- Measurement value is `private` and `final` (immutable)
- `equals()` is overridden correctly
- `Double.compare()` is used instead of `==`
- Handles `null`, type mismatch, and same reference cases safely

---

# Quantity Measurement App – UC3 (Generic Quantity Length)

### 📌 Overview

- This module refactors Feet and Inches into a **single generic Length class**.
- It eliminates code duplication by applying the **DRY (Don’t Repeat Yourself) principle**.
- Supports equality comparison **across different units** (feet ↔ inches).


### ⚙️ Use Case: UC3 – Generic Quantity Length Equality

- Accepts two numerical values along with their respective unit types
- Converts different units to a **common base unit**
- Compares values for equality
- Returns `true` if equivalent, otherwise false


### ⚙️ Key Implementation Points

- Uses a **single Length class** to represent all length measurements
- Introduces a `LengthUnit` **enum** for supported units and conversion factors
- Eliminates separate Feet and Inches classes
- Conversion logic is centralised and reusable
- Measurement value and unit are **encapsulated**
- `equals()` is overridden for **cross-unit value-based equality**
- Uses safe floating-point comparison
- Handles:

  - `null` values
  - invalid units
  - same reference checks
  - type mismatch safely
  
---

# Quantity Measurement App – UC4 (Extended Unit Support)

### 📌 Overview
 
- This module extends the generic Length class introduced in UC3 by adding support for Yards and Centimeters.
- It demonstrates how a well-designed generic solution scales to new units without code duplication.
- Supports equality comparison across `feet ↔ inches ↔ yards ↔ centimeters`.

### ⚙️ Use Case: UC4 – Extended Quantity Length Equality

- Accepts two numerical values along with their respective unit types
- Supports additional units: `YARDS` and `CENTIMETERS`
- Converts different units to a common base unit
- Compares values for equality
- Returns `true` if equivalent, otherwise `false`

### ⚙️ Key Implementation Points

- Continues using the single generic Length class
- Extends the existing LengthUnit enum with:
- YARDS `(1 yard = 3 feet)`
- CENTIMETERS `(1 cm = 0.393701 inches)`
- No changes required in Length class logic
- Conversion logic remains centralised in the enum
- Measurement value and unit stay encapsulated
- `equals()` supports cross-unit comparisons seamlessly
- Uses safe `floating-point comparison`

---


