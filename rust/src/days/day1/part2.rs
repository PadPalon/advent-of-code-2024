use std::collections::HashMap;
use std::fs;
use std::ops::Mul;

pub fn run() {
    let input =
        fs::read_to_string("inputs/day1/input").expect("should have been able to read input");
    let split = input.split_whitespace();
    let mut left_numbers: Vec<u32> = vec![];
    let mut right_numbers: HashMap<u32, u32> = HashMap::new();
    for (index, number) in split.enumerate() {
        let number = number.parse().expect("line contained invalid number");
        if index % 2 == 0 {
            left_numbers.push(number);
        } else {
            right_numbers.entry(number).and_modify(|count| *count += 1).or_insert(1);
        }
    }
    let sum: u32 = left_numbers
        .iter()
        .map(|left_number| {
            return left_number.mul(right_numbers.get(left_number).unwrap_or(&0));
        })
        .sum();
    println!("sum of similarities is {sum}");
}
