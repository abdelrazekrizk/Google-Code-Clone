<?php

/**
 * School filter form base class.
 *
 * @package    gtonline
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormFilterGeneratedTemplate.php 29570 2010-05-21 14:49:47Z Kris.Wallsmith $
 */
abstract class BaseSchoolFormFilter extends BaseFormFilterDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'name' => new sfWidgetFormFilterInput(array('with_empty' => false)),
      'type' => new sfWidgetFormChoice(array('choices' => array('' => '', 'College/University' => 'College/University', 'High School' => 'High School', 'Middle School' => 'Middle School', 'Elementary School' => 'Elementary School'))),
    ));

    $this->setValidators(array(
      'name' => new sfValidatorPass(array('required' => false)),
      'type' => new sfValidatorChoice(array('required' => false, 'choices' => array('College/University' => 'College/University', 'High School' => 'High School', 'Middle School' => 'Middle School', 'Elementary School' => 'Elementary School'))),
    ));

    $this->widgetSchema->setNameFormat('school_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'School';
  }

  public function getFields()
  {
    return array(
      'id'   => 'Number',
      'name' => 'Text',
      'type' => 'Enum',
    );
  }
}
