<?php

/**
 * School form base class.
 *
 * @method School getObject() Returns the current form's model object
 *
 * @package    gtonline
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormGeneratedTemplate.php 29553 2010-05-20 14:33:00Z Kris.Wallsmith $
 */
abstract class BaseSchoolForm extends BaseFormDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'   => new sfWidgetFormInputHidden(),
      'name' => new sfWidgetFormInputText(),
      'type' => new sfWidgetFormChoice(array('choices' => array('College/University' => 'College/University', 'High School' => 'High School', 'Middle School' => 'Middle School', 'Elementary School' => 'Elementary School'))),
    ));

    $this->setValidators(array(
      'id'   => new sfValidatorChoice(array('choices' => array($this->getObject()->get('id')), 'empty_value' => $this->getObject()->get('id'), 'required' => false)),
      'name' => new sfValidatorString(array('max_length' => 255)),
      'type' => new sfValidatorChoice(array('choices' => array(0 => 'College/University', 1 => 'High School', 2 => 'Middle School', 3 => 'Elementary School'))),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorDoctrineUnique(array('model' => 'School', 'column' => array('name')))
    );

    $this->widgetSchema->setNameFormat('school[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'School';
  }

}
