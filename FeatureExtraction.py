# -*- coding: utf-8 -*-
"""
Created on Sun Feb 27 13:02:35 2022

@author: 24508
"""

import os, csv, sys, numpy as np

directory = "data"
num_features = int(sys.argv[1])
window_size = int(sys.argv[2])*1000
sliding_length = 1000
if num_features == 2:
    feature_set = ["mean_x", "std_x", "mean_y", "std_y", "mean_z", "std_z", "Activity"]
else:
    feature_set = ["mean_x", "std_x", "median_x", "root_mean_square_x", "mean_y", "std_y", "median_y", "root_mean_square_y", "mean_z", "std_z", "median_z", "root_mean_square_z", "Activity"]

# open('features.csv', 'w').close()

with open('features.csv', 'w', newline='') as csvfile:
    csvwriter = csv.writer(csvfile)
    csvwriter.writerow(feature_set)
    csvfile.close()

for filename in os.listdir(directory):
    X = []
    Y = []
    Z = []
    time = []
    features = []
    activity = ""
    
    f = os.path.join(directory, filename)
    if os.path.isfile(f):
        if "not" in filename:
            activity = "no_hand_wash"
        else:
            activity = "hand_wash"

        with open(f, 'r') as csvfile:
            csvreader = csv.reader(csvfile)
            next(csvreader)
            first = next(csvreader)
            window_ending = int(first[0]) + window_size
            time.append(int(first[0]))
            X.append(float(first[3]))
            Y.append(float(first[4]))
            Z.append(float(first[5]))
            for row in csvreader:
                if int(row[0]) > window_ending:
                    window_ending += sliding_length
                    feature = []
                    feature.append(np.mean(X))
                    feature.append(np.std(X))
                    if num_features != 2:
                        feature.append(np.median(X))
                        feature.append(np.sqrt(np.mean(np.square(X))))
                    feature.append(np.mean(Y))
                    feature.append(np.std(Y))
                    if num_features != 2:
                        feature.append(np.median(Y))
                        feature.append(np.sqrt(np.mean(np.square(Y))))
                    feature.append(np.mean(Z))
                    feature.append(np.std(Z))
                    if num_features != 2:
                        feature.append(np.median(Z))
                        feature.append(np.sqrt(np.mean(np.square(Z))))
                    feature.append(activity)
                    features.append(feature)
                    while True:
                        if len(time) < 1 or time[0] > window_ending - window_size:
                            break
                        time.pop(0)
                        X.pop(0)
                        Y.pop(0)
                        Z.pop(0)
                time.append(int(row[0]))
                X.append(float(row[3]))
                Y.append(float(row[4]))
                Z.append(float(row[5]))
            csvfile.close()
        
        with open('features.csv', 'a', newline='') as csvfile:
            csvwriter = csv.writer(csvfile)
            csvwriter.writerows(features)
            csvfile.close()
