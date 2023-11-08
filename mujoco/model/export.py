import bpy
from bpy import context
import xml.etree.ElementTree as ET

import builtins as __builtin__

import builtins as __builtin__

# def console_print(*args, **kwargs):
#     for a in context.screen.areas:
#         if a.type == 'CONSOLE':
#             c = {}
#             c['area'] = a
#             c['space_data'] = a.spaces.active
#             c['region'] = a.regions[-1]
#             c['window'] = context.window
#             c['screen'] = context.screen
#             s = " ".join([str(arg) for arg in args])
#             for line in s.split("\n"):
#                 bpy.ops.console.scrollback_append(c, text=line)

# def print(*args, **kwargs):
#     """Console print() function."""

#     console_print(*args, **kwargs) # to py consoles
#     __builtin__.print(*args, **kwargs) # to system console

root = ET.Element('mujoco')
root.set('model', 'model')

compiler = ET.SubElement(root, 'compiler')
compiler.set('angle', 'radian')
compiler.set('meshdir', 'assets')
compiler.set('autolimits', 'true')

defaults = ET.SubElement(root, 'default')

material = ET.SubElement(defaults, 'material')
material.set('specular', '0.5')
material.set('shininess', '0.25')

visual = ET.SubElement(defaults, 'default')
visual.set('class', 'visual')

visual_geom = ET.SubElement(visual, "geom")
visual_geom.set('type', 'mesh')
visual_geom.set('contype', '0')
visual_geom.set('conaffinity', '0')
visual_geom.set('group', '2')

collision = ET.SubElement(defaults, "default")
collision.set('class', 'collision')
collision_geom = ET.SubElement(collision, 'geom')
collision_geom.set('type', 'mesh')
collision_geom.set('group', '3')

visual_scene = ET.SubElement(root, 'visual')
headlight = ET.SubElement(visual_scene, 'headlight')
headlight.set('diffuse', '0.6 0.6 0.6')
headlight.set('ambient', '0.3 0.3 0.3')
headlight.set('specular', '0 0 0')
rgba = ET.SubElement(visual_scene, 'rgba')
rgba.set('haze', '0.15 0.25 0.35 1')
global_scene = ET.SubElement(visual_scene, 'global')
global_scene.set('azimuth', '120')
global_scene.set('elevation', '-20')

assets = ET.SubElement(root, 'asset')
skybox = ET.SubElement(assets, 'texture')
skybox.set('type', 'skybox')
skybox.set('builtin', 'gradient')
skybox.set('rgb1', '0.3 0.5 0.7')
skybox.set('rgb2', '0 0 0')
skybox.set('width', '512')
skybox.set('height', '3072')

ground = ET.SubElement(assets, 'texture')
ground.set('type', '2d')
ground.set('name', 'groundplane')
ground.set('builtin', 'checker')
ground.set('mark', 'edge')
ground.set('rgb1', '0.2 0.3 0.4')
ground.set('rgb2', '0.1 0.2 0.3')
ground.set('markrgb', '0.8 0.8 0.8')
ground.set('width', '300')
ground.set('height', '300')

ground_material = ET.SubElement(assets, 'material')
ground_material.set('name', 'groundplane')
ground_material.set('texture', 'groundplane')
ground_material.set("texuniform", 'true')
ground_material.set('texrepeat', '5 5')
ground_material.set('reflectance', '0.2')

frame = ET.SubElement(assets, 'mesh')
frame.set('file', 'frame.stl')

center = ET.SubElement(assets, 'mesh')
center.set('file', 'center.stl')

roller = ET.SubElement(assets, 'mesh')
roller.set('file', 'roller.stl')

worldbody = ET.SubElement(root, 'worldbody')

base = ET.SubElement(worldbody, "body")
frame = bpy.data.objects["frame"]
base.set('pos', str(frame.location[0]) + " " + str(frame.location[1]) + " " + str(frame.location[2]))
base_joint = ET.SubElement(base, 'joint')
base_joint.set('type', 'free')
base_geom = ET.SubElement(base, 'geom')
base_geom.set('type', 'mesh')
base_geom.set('mesh', 'frame')

def f(current, parent):
    for _child in current.children:
        child = ET.SubElement(parent, 'body')
        child.set('pos', str(_child.location[0]) + " " + str(_child.location[1]) + " " + str(_child.location[2]))
        child.set('quat', str(_child.rotation_euler.to_quaternion()[0]) + " " + str(_child.rotation_euler.to_quaternion()[1]) + " " + str(_child.rotation_euler.to_quaternion()[2]) + " " + str(_child.rotation_euler.to_quaternion()[3]))
        child_geom = ET.SubElement(child, 'geom')
        child_geom.set('type', 'mesh')
        child_geom.set('mesh', _child.name.split(".")[0])

        child_joint = ET.SubElement(child, 'joint')
        child_joint.set('axis', '1 0 0')

        if(len(current.children) is 0):
            return
        f(_child, child)

f(frame, base)

light = ET.SubElement(worldbody, 'light')
light.set('pos', '0 0 1.5')
light.set('dir', '0 0 -1')
light.set('directional', 'true')
plane = ET.SubElement(worldbody, 'geom')
plane.set('name', 'floor')
plane.set('size', '0 0 0.05')
plane.set('type', 'plane')
plane.set('material', 'groundplane')

 
b_xml = ET.tostring(root)

with open(r"C:\ProgramData\mujoco\model\test.xml", "wb") as f:
    f.write(b_xml)
