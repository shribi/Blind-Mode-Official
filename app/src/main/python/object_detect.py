from ast import ClassDef
from statistics import mode
import cv2                                                      #pip install opencv-python
import matplotlib.pyplot as plt                                 #pip install matplotlib
def main(camIndex):
    config_file = 'assets/ssd_mobilenet_v3_large_coco_2020_01_14.pbtxt'
    frozen_model = 'assets/frozen_inference_graph.pb'
    model = cv2.dnn_DetectionModel(frozen_model, config_file)
    classLabels = []
    file_name = "assets/Labels.txt"
    with open(file_name, 'rt') as fpt:
        classLabels = fpt.read().strip('\n').split('\n')

    model.setInputSize(320, 320)
    model.setInputScale(1.0/127.5)
    model.setInputMean((127.5, 127.5, 127.5))
    model.setInputSwapRB(True)
    

    cap = cv2.VideoCapture(camIndex)
    font_scale = 1
    font = cv2.FONT_HERSHEY_SIMPLEX

    while True:
        ret, frame = cap.read()

        ClassIndex, confidence, bbox = model.detect(frame, confThreshold = 0.55)
        if(len(ClassIndex)!=0):
            for ClassInd, conf, boxes in zip(ClassIndex.flatten(), confidence.flatten(), bbox):
                if (ClassInd <= 80):
                    cv2.rectangle(frame, boxes, (255, 0, 0), 2)
                    cv2.putText(frame, classLabels[ClassInd-1], (boxes[0]+10, boxes[1]+40), font, fontScale=font_scale, color = (0, 0, 255), thickness=1)

        cv2.imshow('Blind Mode', frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            return len(classLabels)
            break
    cap.release()
    cv2.destroyAllWindows()
